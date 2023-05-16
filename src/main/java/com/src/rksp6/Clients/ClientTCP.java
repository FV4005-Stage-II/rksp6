package com.src.rksp6.Clients;

import com.src.rksp6.FileManager;
import com.src.rksp6.Servers.ServerRequest;

import java.io.*;
import java.net.Socket;

public class ClientTCP implements IClient {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean isConnected;
    private String ip;
    private int port;

    public ClientTCP(String ip, int port) {
        isConnected = false;
        this.ip = ip;
        this.port = port;
    }

    public boolean startConnection() {
        try {
            socket = new Socket(ip, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            isConnected = true;

            return true;
        } catch(Exception exception) {
            stopConnection();
            isConnected = false;
            exception.printStackTrace();
        }

        return false;
    }

    public void send(byte[] array){
        startConnection();
        if(!getConnectionStatus())
            return;

        try {
            out.writeInt(array.length);
            out.write(array);
        } catch (Exception ex){
            stopConnection();
            ex.printStackTrace();
        }
    }

    public void send(String message){
        send(message.getBytes());
    }

    public void send(File file){
        try {
            send(FileManager.readFile(file.getAbsolutePath()).getBytes());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void send(Object object){
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput objectOutput = new ObjectOutputStream(bos)) {
            objectOutput.writeObject(object);
            this.send(bos.toByteArray());
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
            socket.close();
            out.close();
            in.close();
            isConnected = false;
            return true;
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return false;
    }

    public String request(ServerRequest request){
        this.send(request.toString().getBytes());

        var received = "Server did not respond";

        try {
            var length = in.readInt();
            if (length > 0) {
                var message = new byte[length];
                in.readFully(message, 0, message.length);
                received = new String(message);
            }
        } catch (Exception ex){
            stopConnection();
            ex.printStackTrace();
        }

        stopConnection();
        return received;
    }

    public boolean getConnectionStatus(){
        return isConnected;
    }
}
