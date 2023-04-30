package com.src.rksp6;

import java.io.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import com.src.rksp6.object.*;

public class Controller {

    private ObservableList<String> choiceValue = FXCollections.observableArrayList("Круг", "Текст");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> choice;

    @FXML
    private MenuItem Load;

    @FXML
    private MenuItem Save;

    @FXML
    private Pane FieldDraw;

     private Conveyor model = null;
     private SaveOrLoad sol = null;
    private String type = null;

    @FXML
    void initialize() {
        // Tesak is the best!
        model = new Conveyor();
        sol = new SaveOrLoad();
        choice.setItems(choiceValue);
        choice.getSelectionModel().selectFirst();
        type = choice.getSelectionModel().getSelectedItem().toString();
    }

    @FXML
    void select(ActionEvent event) {
        type = choice.getSelectionModel().getSelectedItem().toString();
    }

    @FXML
    void MouseClickedDrawShape(MouseEvent event) throws IOException, ClassNotFoundException {
        double x = event.getX();
        double y = event.getY();
         sol.addShape(this.model.createShape(type, x, y));
         FieldDraw.getChildren().add(sol.gShapes().get(sol.gShapes().size() - 1).drawObject());
    }

     @FXML
     void load(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Load file");
         fileChooser.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter("Text", "*.txt"),
                 new FileChooser.ExtensionFilter("Binary", "*.bin"),
                 new FileChooser.ExtensionFilter("XML", "*.xml"),
                 new FileChooser.ExtensionFilter("JSON", "*.json"));
         File selectedFile = fileChooser.showOpenDialog(FieldDraw.getScene().getWindow());
         if (selectedFile != null) {
             try {
                 sol.load(selectedFile.getPath());
                 FieldDraw.getChildren().clear(); // удаление всех объектов
                 for (objShape obj : sol.gShapes())
                     FieldDraw.getChildren().addAll(obj.drawObject());
             } catch (Exception e) {
                 System.out.println(e);
             }
         }
     }

     @FXML
     void save(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Save file");
         fileChooser.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter("Text", "*.txt"),
                 new FileChooser.ExtensionFilter("Binary", "*.bin"),
                 new FileChooser.ExtensionFilter("XML", "*.xml"),
                 new FileChooser.ExtensionFilter("JSON", "*.json"));
         File selectedFile = fileChooser.showSaveDialog(FieldDraw.getScene().getWindow());
         if (selectedFile != null) {
             try {
                 sol.save(selectedFile.getPath());
             } catch (IOException e) {
                 System.out.println(e.getMessage());
             }
         }
     }
}