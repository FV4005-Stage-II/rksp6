package com.src.rksp6.object;

import java.util.ArrayList;
import javafx.scene.layout.Pane;

public class Conveyor {
    private ArrayList<objShape> shapes;
    private double size = 30;
    Pane FieldDraw;
    private String text = "КЛЮЧ НА 8";

    public Conveyor(Pane FieldDraw) {
        this.shapes = new ArrayList<objShape>();
        this.FieldDraw = FieldDraw;
    }

    // юнит тесты на булеву с возвращением 1
    public objShape createShape(String type, double x, double y) {
        objShape shape = null;
        switch (type) {
            case "Круг" -> shape = new objCircle(x, y, size);
            case "Текст" -> shape = new objText(x, y, size, text);
        }
        return shape;
        // addShape(shape);
        // FieldDraw.getChildren().add(shapes.get(shapes.size() - 1).drawObject());
    }
}
