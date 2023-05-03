package com.src.rksp6;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.src.rksp6.object.*;



public class SaveOrLoad {
    private ArrayList<objShape> shapes = new ArrayList<>();
    public ArrayList<objShape> gShapes() {
        return shapes;
    }
    public void addShape(objShape object) {
        shapes.add(object);
    }

    public void save(String path) throws IOException {
        OutputStream os = new FileOutputStream(path);
        String ext = FilenameUtils.getExtension(path);
        switch (ext) {
            case "txt" -> saveTxt(os);
            case "bin" -> saveBin(os);
            case "xml" -> saveXml(os);
            // case "json" -> saveJson(os);
        }

        os.close();
    }

    public void load(String path) throws Exception {
        String ext = FilenameUtils.getExtension(path);
        InputStream is = new FileInputStream(path);

        shapes.clear();
        switch (ext) {
            case "txt" -> loadTxt(is);
            case "bin" -> loadBin(is);
            case "xml" -> loadXml(is);
            // case "json" -> loadJson(is);
        }
        is.close();
    }

    private void saveBin(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(shapes);
        oos.flush();
        oos.close();
    }

    private void loadBin(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        shapes = (ArrayList<objShape>) ois.readObject();
        ois.close();
    }

    public objShape loadBinTCP(String path) throws Exception{
        InputStream is = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(is);
        objShape shape = (objShape) ois.readObject();
        ois.close();
        return shape;
    }

    private void saveTxt(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(shapes.size() + "\n");
        for (objShape obj : shapes)
            osw.write(obj.string());
        osw.flush();
        osw.close();
    }

    private void loadTxt(InputStream is) throws Exception {
        Scanner scanner = new Scanner(is);

        int count = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < count; i++) {
            String className = scanner.nextLine();
            objShape obj = null;
            switch (className) {
                case "objCircle" ->
                    obj = new objCircle(
                            Double.parseDouble(scanner.nextLine()),
                            Double.parseDouble(scanner.nextLine()),
                            Double.parseDouble(scanner.nextLine()));
                case "objText" ->
                    obj = new objText(
                            Double.parseDouble(scanner.nextLine()),
                            Double.parseDouble(scanner.nextLine()),
                            Double.parseDouble(scanner.nextLine()),
                            scanner.nextLine());
            }
            shapes.add(obj);
        }
        scanner.close();
    }

    private void saveXml(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        XStream xstream = new XStream();
        String dataXml = xstream.toXML(shapes);
        osw.write(dataXml);
        osw.flush();
        osw.close();
    }

    private void loadXml(InputStream is) throws IOException {
        XStream xstream = new XStream();
        String xmlData = IOUtils.toString(is); // , "UTF-8" если вставить кодировка слетает

        xstream.allowTypesByWildcard(new String[] { "com.src.rksp6.**" });
        shapes = (ArrayList<objShape>) xstream.fromXML(xmlData);
    }

}