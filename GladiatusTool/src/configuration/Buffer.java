/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author krkoska.tomas
 */
public class Buffer {

    private final String format = ".ser";

    public void serializableObject(Object object, String FILE) throws FileNotFoundException, IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(new File(FILE + format)));

        outputStream.writeObject(object);
        outputStream.close();
    }

    protected Object deserializableObject(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName + format);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

}
