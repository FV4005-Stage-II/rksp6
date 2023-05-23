package com.src.rksp6.Servers;

import com.src.rksp6.FileManager;
import com.src.rksp6.object.Conveyor;
import com.src.rksp6.object.objShape;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private ServerMemory mem;

    public ServerTCP(int port, ServerMemory _serialization){
        try {
            serverSocket = new ServerSocket(port);
            mem = _serialization;
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void start(){
        try {
            while(true){
                clientSocket = serverSocket.accept();
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Connection");

                var length = in.readInt();
                if (length > 0) {
                    var data = new byte[length];
                    in.readFully(data);
                    var answer = this.processCommand(new String(data), data);

                    if (answer == "Server received shape.")
                        continue;

                    out.writeInt(answer.length());
                    out.write(answer.getBytes());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String processCommand(String request, byte[] object) {
        var answer = "Unknown request.";

        switch (request) {
            case ("SHAPES") -> {
                /*FileManager.serialize(mem.getShapes(), "SHAPES.bin");
                FileManager.outputFile(out, "SHAPES.bin", 1024);*/
                answer = mem.getStringShapes();
            }
            case ("NAMES") -> {
                /*FileManager.writeToFile(mem.namesShapes(), "NAMES.bin");
                FileManager.outputFile(out, "NAMES.bin", 1024);*/
                answer =  mem.getNames();
            }
            case ("QUANTITY") -> {
                /*FileManager.writeToFile(Integer.toString(mem.getShapes().size()),
                        "QUANTITY.bin");
                FileManager.outputFile(out, "QUANTITY.bin", 1024);*/
                answer = Integer.toString(mem.getShapes().size());
            }
            case ("CLEAR") -> {
                mem.clearShapes();
                answer = "Cleared";
            }
            default -> {
                //SaveSerializedShape(shapeFactory, clientShapeData, fileName);
                try{
                    var shape = (objShape)convertFromBytes(object);
                    if(shape != null){
                        mem.getShapes().add(shape);
                        answer = "Server received shape.";
                    } else {
                        throw new StreamCorruptedException();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        return answer;
    }

    public static Object convertFromBytes(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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
            mem.getShapes().add(shape);
            FileManager.outputFile(out,"shape.bin", 1024);
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
        in.close();
        out.close();
        serverSocket.close();
        clientSocket.close();
    }

    public static void main(String[] args){
        var serialization = new ServerMemory();
        while(true) {
            var server = new ServerTCP(4443, serialization);
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
