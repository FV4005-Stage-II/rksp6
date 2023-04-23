package com.src.rksp6.object;

import javafx.util.Duration;

import java.io.Serializable;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.css.Size;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class objText extends objShape implements Serializable {
    private String text;
    private transient Text object;
    private transient TranslateTransition transition;

    public objText(double positionX, double positionY, double size, String text) {
        super(positionX, positionY, size);
        this.text = text;
    }

    public String gText() {
        return text;
    }

    @Override
    protected void createNode() {
        object = new Text(gX(), gY(), this.text);
        object.setFont(Font.font("Verdana", FontPosture.ITALIC, gSize()));
    }

    @Override
    protected void createMove() {
        transition = new TranslateTransition();
        transition.setNode(object);
        transition.setDuration((Duration.millis(1500)));
        transition.setByX(400);
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
        return "objText\n" + gX() + "\n" + gY() + "\n" + gSize() + "\n" + gText() + "\n";
    }

}
