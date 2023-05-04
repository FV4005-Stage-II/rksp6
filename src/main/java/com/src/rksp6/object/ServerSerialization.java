package com.src.rksp6.object;

import java.util.ArrayList;

public class ServerSerialization {
    private ArrayList<objShape> shapes;
    public ServerSerialization(){
        shapes = new ArrayList<objShape>();
    }

    public void clearShapes() { getShapes().clear(); }
    public int quantityShapes() { return getShapes().size(); }
    public String[] namesShapes() {
        String[] names = new String[getShapes().size()];
        int i = 0;
        for (objShape shape : getShapes()) {
            names[i] = shape.name();
            i++;
        }
        return names;
    }

    public ArrayList<objShape> getShapes() { return shapes; }

    public void addShape(objShape object) {
        shapes.add(object);
    }
}
