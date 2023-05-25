package com.src.rksp6.object;

import java.util.ArrayList;


public class Conveyor {
    private ArrayList<objShape> shapes;
    private double size = 30;
    private String text = "КЛЮЧ НА 8";

    public Conveyor() {
        this.shapes = new ArrayList<objShape>();
    }
    public objShape createShape(String type, double x, double y) {
        objShape shape = null;
        switch (type) {
            case "Circle" -> shape = new objCircle(x, y, size);
            case "Text" -> shape = new objText(x, y, size, text);
        }
        return shape;
    }

    public objShape createShape(String shapeData){
        var arr = shapeData.split(";");
        return createShape(arr[0], Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
    }
}
