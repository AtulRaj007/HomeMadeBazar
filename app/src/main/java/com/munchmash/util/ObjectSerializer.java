package com.munchmash.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Atul on 8/9/17.
 */

public class ObjectSerializer {
    public static String serialize(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public static Object deserialize(String data) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(
                data, Base64.DEFAULT));
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object result;
        result = ois.readObject();
        ois.close();
        return result;
    }
}