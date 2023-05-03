package com.src.rksp6.object;

import java.io.Serializable;

import javafx.scene.Node;


public abstract class objShape implements Serializable {
    private double positionX = 0;
    private double positionY = 0;
    private double size = 0;

    public objShape(double positionX, double positionY, double size) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
    }

    public double gX() {
        return positionX;
    }

    public double gY() {
        return positionY;
    }

    public double gSize() {
        return size;
    }

    protected abstract void createNode();

    protected abstract void createMove();

    public abstract String string();

    public abstract Node drawObject();

    public abstract String name();

}
