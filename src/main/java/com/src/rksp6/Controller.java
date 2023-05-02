package com.src.rksp6;

import java.io.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    @FXML
    private ComboBox<String> choiceTCP;
    @FXML
    private Pane FieldDrawTCP;

    @FXML
    private ToggleButton StateTCP;

    @FXML
    private Button BtnShapesRequest;

    @FXML
    private Button BtnClearServerShapes;

    @FXML
    private Button BtnRequestNames;

    @FXML
    private Button BtnRequestQuantity;

    @FXML
    private TextFlow FieldMessage;
    ///////////////////////////////
    private Conveyor model = null;
    private SaveOrLoad sol = null;
    private String type = null;
    private String typeTCP = null;


    @FXML
    void initialize() {
        // Tesak is the best!!!!
        // следы зубоб на гриндерах
        model = new Conveyor();
        sol = new SaveOrLoad();

        choice.setItems(choiceValue);
        choice.getSelectionModel().selectFirst();
        choiceTCP.setItems(choiceValue);
        choiceTCP.getSelectionModel().selectFirst();

        type = choice.getSelectionModel().getSelectedItem().toString();
        typeTCP = choice.getSelectionModel().getSelectedItem().toString();

        Text t1 = new Text("хуй\n");
        Text t2 = new Text("залупа\n");
        FieldMessage.getChildren().addAll(t1, t2);

    }

    @FXML
    void select(ActionEvent event) {
        type = choice.getSelectionModel().getSelectedItem().toString();
    }
    @FXML
    void selectTCP(ActionEvent event) {
        typeTCP = choiceTCP.getSelectionModel().getSelectedItem().toString();
    }
    @FXML
    void setState(MouseEvent event) {
        if (StateTCP.isSelected())
            StateTCP.setText("НА СВЯЗИ");
        else
            StateTCP.setText("ДО СВЯЗИ");
    }
    @FXML
    void MouseClickedDrawShape(MouseEvent event) throws IOException, ClassNotFoundException {
        double x = event.getX();
        double y = event.getY();
        sol.addShape(this.model.createShape(type, x, y));
        FieldDraw.getChildren().add(sol.gShapes().get(sol.gShapes().size() - 1).drawObject());
    }
    @FXML
    void MouseClickedDrawShapeTCP(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
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

    @FXML
    void shapesRequest(MouseEvent event) {

    }

    @FXML
    void requestNames(MouseEvent event) {

    }

    @FXML
    void requestQuantity(MouseEvent event) {

    }

    @FXML
    void clearServerShapes(MouseEvent event) {

    }
}