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

    public static void outputFile(DataOutputStream dout, String filePath, int bufferSize){
        var file = new File(filePath);

        try(var fileInputStream = new FileInputStream(file)) {

            dout.writeLong(file.length());

            var buffer = new byte[bufferSize];
            int bytes = 0;
            while ((bytes = fileInputStream.read(buffer)) != -1) {
                dout.write(buffer, 0, bytes);
                dout.flush();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void writeToFile(String[] strings, String fileName){
        File fout = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(fout);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
            for (String s : strings) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException ignored) {

        }
    }

    public static void writeToFile(String string, String fileName){
        File fout = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(fout);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
             bw.write(string);
        } catch (IOException ignored) {

        }
    }
}
