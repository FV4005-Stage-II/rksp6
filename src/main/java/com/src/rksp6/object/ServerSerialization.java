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
            shapes.add(shape);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<objShape> getShapes() { return shapes; }

    public void addShape(objShape object) {
        shapes.add(object);
    }
}
