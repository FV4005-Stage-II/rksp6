package com.src.rksp6.object;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServerSerialization {
    private ArrayList<objShape> shapes;
    public void ServerSerialization(){
        shapes = new ArrayList<objShape>();
    }
    public void Serialize(objShape shape, String fileName) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(shape);
            oos.flush();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void SerializeShapes(String fileName) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(getShapes());
            oos.flush();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void clearShapes() { getShapes().clear(); }
    public int quantityShapes() { return getShapes().size(); }
    public String[] namesShapes() {
        String[] names = new String[getShapes().size()];
        int i = 0;
        for (objShape shape : getShapes()) {
            names[i] = shape.string();
            i++;
        }
        return names;
    }

    public ArrayList<objShape> getShapes() { return shapes; }

    public void addShape(objShape object) {
        shapes.add(object);
    }
}
