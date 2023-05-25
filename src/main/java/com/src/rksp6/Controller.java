package com.src.rksp6;

import java.io.*;

import java.net.URL;
import java.util.ResourceBundle;

import com.src.rksp6.Clients.Client;
import com.src.rksp6.Clients.ClientHttp;
import com.src.rksp6.Servers.ServerRequest;
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

    private ObservableList<String> choiceValue = FXCollections.observableArrayList("Circle", "Text");
    private ObservableList<String> choiceConectionList = FXCollections.observableArrayList("TCP", "UDP", "HTTP");

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
    private Button BtnShapesRequest;

    @FXML
    private Button BtnClearServerShapes;

    @FXML
    private Button BtnRequestNames;

    @FXML
    private Button BtnRequestQuantity;

    @FXML
    private TextFlow FieldMessage;

    @FXML
    private ComboBox<String> ChoiceConnection;
    ///////////////////////////////
    private Conveyor model = null;
    private SaveOrLoad sol = null;
    private SaveOrLoad sol1 = null;
    private String type = null;
    private String typeTCP = null;
    private String typeConection = null;
    private Client client;

    @FXML
    void initialize() throws IOException {
        ClientHttp a = new ClientHttp();
        model = new Conveyor();
        sol = new SaveOrLoad();
        sol1 = new SaveOrLoad();

        choice.setItems(choiceValue);
        choice.getSelectionModel().selectFirst();
        choiceTCP.setItems(choiceValue);
        choiceTCP.getSelectionModel().selectFirst();
        ChoiceConnection.setItems(choiceConectionList);
        ChoiceConnection.getSelectionModel().selectFirst();

        type = choice.getSelectionModel().getSelectedItem().toString();
        typeTCP = choiceTCP.getSelectionModel().getSelectedItem().toString();
        typeConection =  ChoiceConnection.getSelectionModel().getSelectedItem().toString();
        client = new Client(typeConection);
    }
    @FXML
    void selectConnection(ActionEvent event) {
        typeConection = ChoiceConnection.getSelectionModel().getSelectedItem().toString();
        client.setActiveClient(typeConection);
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
    void MouseClickedDrawShape(MouseEvent event) throws IOException, ClassNotFoundException {
        double x = event.getX();
        double y = event.getY();
        sol.addShape(this.model.createShape(type, x, y));
        FieldDraw.getChildren().add(sol.gShapes().get(sol.gShapes().size() - 1).drawObject());
    }
    @FXML
    void MouseClickedDrawShapeTCP(MouseEvent event) throws Exception {

        double x = event.getX();
        double y = event.getY();
        sol1.addShape(this.model.createShape(typeTCP, x, y));
        var shape = sol1.gShapes().get(sol1.gShapes().size() - 1);
        FieldDrawTCP.getChildren().add(shape.drawObject());
        client.send(shape);
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
    void shapesRequest(MouseEvent event) throws Exception {
        Conveyor shape = new Conveyor();
        String namesShapes = client.request(ServerRequest.SHAPES) + "\n";
        FieldMessage.getChildren().addAll(new Text(namesShapes));
        var array = namesShapes.split(";");
        for(var names : array) {
            var name = names.split(" ");
            FieldDrawTCP.getChildren().add(shape.createShape(name[0],
                    Double.parseDouble(name[1]),
                    Double.parseDouble(name[2])).drawObject());
        }
    }

    @FXML
    void requestNames(MouseEvent event) throws Exception {
        var names = client.request(ServerRequest.NAMES) + "\n";
        FieldMessage.getChildren().addAll(new Text(names));
    }

    @FXML
    void requestQuantity(MouseEvent event) throws Exception {
        FieldMessage.getChildren().addAll(new Text(client.request(ServerRequest.QUANTITY) + "\n"));
    }

    @FXML
    void clearServerShapes(MouseEvent event) throws Exception {
        System.out.println(client.request(ServerRequest.CLEAR));
    }
}