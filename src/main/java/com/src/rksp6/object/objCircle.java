package com.src.rksp6.object;

import java.io.Serializable;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class objCircle extends objShape implements Serializable {
    private transient Circle object;
    private transient TranslateTransition transition;
    // private transient Paint color = javafx.scene.paint.Color.GOLD;

    public objCircle(double positionX, double positionY, double radius) {
        super(positionX, positionY, radius);
    }

    @Override
    protected void createNode() {
        object = new Circle(gX(), gY(), gSize());
    }

    @Override
    protected void createMove() {
        transition = new TranslateTransition();
        transition.setNode(object);
        transition.setDuration((Duration.millis(1000)));
        transition.setByX(250);
        transition.setByY(250);
        transition.setCycleCount(Transition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }

    @Override
    public Node drawObject() {
        createNode();
        createMove();
        return object;
    }

    @Override
    public String string() {
        return "objCircle\n" + gX() + "\n" + gY() + "\n" + gSize() + "\n";
    }
}
