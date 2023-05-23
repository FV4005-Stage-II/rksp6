package com.src.rksp6.Servers;

import com.src.rksp6.object.objShape;

import java.util.ArrayList;

public class ServerMemory {
    private ArrayList<objShape> shapes;
    public ServerMemory(){
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

    public String getStringShapes(){
        var strShapes = new ArrayList<String>();

        for(var shape : shapes){
            var strShape = String.join(
                    " ",
                    shape.name(),
                    Double.toString(shape.gX()),
                    Double.toString(shape.gY())
            );
            strShapes.add(strShape);
        }

        return  String.join(";", strShapes);
    }

    public String getNames(){
        var names = new ArrayList<String>();
        for(var shape : shapes)
            names.add(shape.name());

        return String.join("\n", names);
    }

    public void addShape(objShape object) {
        shapes.add(object);
    }
}
