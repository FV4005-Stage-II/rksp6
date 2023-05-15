package com.src.rksp6.Clients;

import com.src.rksp6.Servers.ServerRequest;

import java.io.*;
import java.net.Socket;

public class ClientTCP implements IClient {
    private Socket clientSocket;
    private PrintWriter out;
    private DataInputStream in;
    private boolean isConnected;

    public ClientTCP(){
        isConnected = false;
    }

    public boolean startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new DataInputStream(clientSocket.getInputStream());
            isConnected = true;

            return true;
        } catch(Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void sendShape(String shape) {
        out.println(shape);
        out.flush();
        receiveFile("receivedShape.bin", 1024);
    }

    public void send(String message){
        out.println(message);
        out.flush();
        if(!message.equals("CLEAR"))
            receiveFile("received"+ message + ".bin", 1024);
    }

    public void send(File file){
        throw new UnsupportedOperationException();
    }

    private void receiveFile(String path, int bufferSize) {
        try(var fileOutputStream = new FileOutputStream(path)) {
            int bytes = 0;
            // Read file size
            long size = in.readLong();
            byte[] buffer = new byte[bufferSize];

            while (size > 0 && (bytes = in.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                // Read upto file size
                size -= bytes;
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean stopConnection() {
        try {
            clientSocket.close();
            out.close();
            in.close();
            isConnected = false;
            return true;
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return false;
    }

    public void send(Object object){
        throw new UnsupportedOperationException();
    }

    public String request(ServerRequest request){
        throw new UnsupportedOperationException();
    }

    public boolean getConnectionStatus(){
        return isConnected;
    }
}
