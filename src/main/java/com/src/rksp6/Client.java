package com.src.rksp6;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private DataInputStream in;
    private boolean isConnected;

    public Client(){
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

    public String sendMessage(String message, String fileName, int bufferSize) {
        out.println(message);
        try {
            message = in.readLine();
            receiveFile(fileName, bufferSize);
            System.out.println("File has been written.");
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return message;
    }

    private void receiveFile(String path, int bufferSize) {
        try(var fileOutputStream = new FileOutputStream(path, true)) {
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

            return true;
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return false;
    }

    public boolean getConnectionStatus(){
        return isConnected;
    }
}
