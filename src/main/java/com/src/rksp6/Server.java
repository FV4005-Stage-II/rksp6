package com.src.rksp6;

import com.src.rksp6.object.Conveyor;
import com.src.rksp6.object.ServerSerialization;
import com.src.rksp6.object.objShape;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private BufferedReader inputReader;
    private ServerSerialization serialization;

    public Server(int port, ServerSerialization _serialization){
        try {
            serverSocket = new ServerSocket(port);
            serialization = _serialization;
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void start(){
        try {
            clientSocket = serverSocket.accept();
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            var shapeFactory = new Conveyor();
            System.out.println("Connection");

            String clientShapeData;
            String fileName = "shape.bin";
            while((clientShapeData = inputReader.readLine()) != null){
                processCommand(shapeFactory, clientShapeData, fileName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processCommand(Conveyor shapeFactory, String clientShapeData, String fileName) {
        switch (clientShapeData) {
            case ("SHAPES") -> {
                FileManager.serialize(serialization.getShapes(), "SHAPES.bin");
                FileManager.outputFile(dataOutputStream, "SHAPES.bin", 1024);
            }
            case ("NAMES") -> {
                FileManager.writeToFile(serialization.namesShapes(), "NAMES.bin");
                FileManager.outputFile(dataOutputStream, "NAMES.bin", 1024);
            }
            case ("QUANTITY") -> {
                FileManager.writeToFile(Integer.toString(serialization.getShapes().size()),
                        "QUANTITY.bin");
                FileManager.outputFile(dataOutputStream, "QUANTITY.bin", 1024);
            }
            case ("CLEAR") -> serialization.getShapes().clear();
            default -> SaveSerializedShape(shapeFactory, clientShapeData, fileName);
        }
    }

    private void SaveSerializedShape(Conveyor shapeFactory, String clientShapeData, String fileName) {
        var shapeData = getDataFromString(clientShapeData);
        var shape = shapeFactory.createShape((String)shapeData[0],
                (double)shapeData[1],
                (double)shapeData[2]);
        System.out.println(clientShapeData);
        System.out.println(shape.string());
        try {
            FileManager.serialize(shape, fileName);
            serialization.getShapes().add(shape);
            FileManager.outputFile(dataOutputStream,"shape.bin", 1024);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Object[] getDataFromString(String shapeDataString){
        var data = new Object[3];
        var splitData = shapeDataString.split(";");

        if(splitData.length < 3)
            return data;

        data[0] = splitData[0];
        for(var i = 1; i < 3; ++i){
            data[i] = Double.parseDouble(splitData[i]);
        }

        return data;
    }

    public void stop() throws IOException {
        inputReader.close();
        dataOutputStream.close();
        serverSocket.close();
        clientSocket.close();
    }

    public static void main(String[] args){
        var serialization = new ServerSerialization();
        while(true) {
            var server = new Server(6666, serialization);
            System.out.println("server init");
            server.start();
            try {
                server.stop();
                System.out.println("server stop");
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
