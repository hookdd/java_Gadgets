package cc;

import java.io.*;
import java.lang.reflect.Field;

public class SerializableTest {
    public static void searilze(Object obj,String filename) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(obj);
    }
    public static Object unzearilze(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        Object readObject = ois.readObject();
        return readObject;
    }
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj,value);
    }
}
