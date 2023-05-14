package com.src.rksp6;

import java.io.*;

import java.net.URL;
import java.util.ResourceBundle;

import com.src.rksp6.Clients.Client;
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
    private ToggleButton ServerType;

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
    private Client client;

    @FXML
    void initialize() {
        model = new Conveyor();
        sol = new SaveOrLoad();

        choice.setItems(choiceValue);
        choice.getSelectionModel().selectFirst();
        choiceTCP.setItems(choiceValue);
        choiceTCP.getSelectionModel().selectFirst();

        type = choice.getSelectionModel().getSelectedItem().toString();
        typeTCP = choiceTCP.getSelectionModel().getSelectedItem().toString();

        client = new Client("UDP");
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
        if (StateTCP.isSelected()) {
            boolean answer = false;

            if(!client.getConnectionStatus())
                answer = client.startConnection("127.0.0.1", 6666);

            if(!answer) {
                StateTCP.setSelected(false);
                FieldMessage.getChildren().add(new Text("нет подключения\n"));
                return;
            }
            StateTCP.setText("НА СВЯЗИ");
        } else {
            boolean answer = false;
            if(client.getConnectionStatus())
                answer = client.stopConnection();

            if(!answer) {
                StateTCP.setSelected(true);
                return;
            }

            StateTCP.setText("ДО СВЯЗИ");
        }
    }

    @FXML
    void changeServerType(MouseEvent event){
        if(ServerType.isSelected()){
            ServerType.setText("UDP");
            StateTCP.setDisable(true);
        } else {
            ServerType.setText("TCP");
            StateTCP.setDisable(false);
        }

        client.setActiveClient(ServerType.getText());
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
        var type = choiceTCP.getSelectionModel().getSelectedItem().toString();
        var str = type + ";" + x + ";" + y;

        if(client.getConnectionStatus())
            client.sendShape(str);
        FieldDrawTCP.getChildren().add((sol.loadBinTCP("receivedShape.bin")).drawObject());
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
        /*client.send(ServerRequest.SHAPES.toString());
        var shapes = sol.loadBinsTCP("receivedSHAPES.bin");
        for(var shape : shapes){
            FieldDrawTCP.getChildren().add(shape.drawObject());
        }*/
        FieldMessage.getChildren().addAll(new Text(client.request(ServerRequest.SHAPES) + "\n"));
    }

    @FXML
    void requestNames(MouseEvent event) throws Exception {
        //String names = FileManager.readFile("receivedNAMES.bin");
        FieldMessage.getChildren().addAll(new Text(client.request(ServerRequest.NAMES) + "\n"));
    }

    @FXML
    void requestQuantity(MouseEvent event) throws Exception {
        //var quantity = FileManager.readFile("receivedQUANTITY.bin");
        FieldMessage.getChildren().addAll(new Text(client.request(ServerRequest.QUANTITY) + "\n"));
    }

    @FXML
    void clearServerShapes(MouseEvent event) throws Exception {
        //client.send(ServerRequest.CLEAR.toString());
        //FieldMessage.getChildren().addAll(new Text(client.request(ServerRequest.CLEAR)));
        System.out.println(client.request(ServerRequest.CLEAR));
    }
}