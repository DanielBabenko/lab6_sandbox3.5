package client;

import client.object.LabWork;

import java.io.*;
import java.util.HashSet;

public class SerializationManager {

    public static byte[] serialize(HashSet<?> collection) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(collection);

        return bos.toByteArray();
    }


    public static byte[] serialize(LabWork labWork) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(labWork);

        return bos.toByteArray();
    }


    public static HashSet<LabWork> deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInputStream ois = new ObjectInputStream(bis);

        return (HashSet<LabWork>) ois.readObject();
    }
}
