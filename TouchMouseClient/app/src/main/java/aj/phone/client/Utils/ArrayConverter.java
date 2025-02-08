package aj.phone.client.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ArrayConverter {
    public static byte[] convertToArrayBytes(float[] data) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static String convertToStringArray(byte[] data) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        String stringArr = (String) objectInputStream.readObject();
        objectInputStream.close();
        return stringArr;
    }
}
