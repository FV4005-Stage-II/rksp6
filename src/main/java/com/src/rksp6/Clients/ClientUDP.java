package com.src.rksp6.Clients;

import com.src.rksp6.FileManager;
import com.src.rksp6.Servers.ServerRequest;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP implements IClient {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    private int port;
    String encoding;

    public ClientUDP(int _port, String _encoding) {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (Exception ex){
            ex.printStackTrace();
        }

        port = _port;
        encoding = _encoding;
        buf = new byte[256];
    }

    public void send(String message) {
        buf = message.getBytes();
        try {
            socket.send(new DatagramPacket(buf, buf.length, address, port));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void send(File file){
        try {
            var fileContents = new String(
                    FileManager.readFile(file.getAbsolutePath()).getBytes(),
                    encoding
            );
            this.send(fileContents);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void send(byte[] array){
        try{
            socket.send(new DatagramPacket(array, array.length, address, port));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void send(Object object){
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            this.send(bos.toByteArray());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String request(ServerRequest request) {
        var messageBuf = request.toString().getBytes();
        var packet = new DatagramPacket(messageBuf,messageBuf.length, address, port);
        var received = "Server did not respond";

        try{
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            var data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
            received =  new String(data, encoding);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return received;
    }

    public void close(){
        socket.close();
    }
}
