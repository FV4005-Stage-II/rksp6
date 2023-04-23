package com.src.rksp6;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        primaryStage.setTitle("Laba1");
        primaryStage.setScene(new Scene(root, 840, 600));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}