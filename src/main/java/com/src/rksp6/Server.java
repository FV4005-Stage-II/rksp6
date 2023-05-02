package com.src.rksp6;

import com.src.rksp6.object.Conveyor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private BufferedReader inputReader;

    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
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
            while((clientShapeData = inputReader.readLine()) != null){
                SendSerializedShape(shapeFactory, clientShapeData, "shape.bin");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void SendSerializedShape(Conveyor shapeFactory, String clientShapeData, String fileName) {
        var sol = new SaveOrLoad();
        var shapeData = getDataFromString(clientShapeData);
        var shape = shapeFactory.createShape((String)shapeData[0],
                (double)shapeData[1],
                (double)shapeData[2]);

        try {
            sol.save(fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Object[] getDataFromString(String shapeDataString){
        var data = new Object[3];
        var splitData = shapeDataString.split(";");

        for(var i = 0; i < 3; ++i){
            data[i] = splitData[i];
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
        while(true) {
            var server = new Server(6666);
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
