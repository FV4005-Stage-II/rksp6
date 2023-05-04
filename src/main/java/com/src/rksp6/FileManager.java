package com.src.rksp6;

import com.src.rksp6.object.objShape;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    public static String readFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buffer = new byte[10];
        StringBuilder sb = new StringBuilder();
        while (fis.read(buffer) != -1) {
            sb.append(new String(buffer));
            buffer = new byte[10];
        }
        fis.close();
        return  sb.toString();
    }

    public static File serialize(objShape shape, String fileName) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(shape);
            oos.flush();
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return new File(fileName);
    }

    public static File serialize(ArrayList<objShape> shapes, String fileName){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(shapes);
            oos.flush();
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return new File(fileName);
    }
}
