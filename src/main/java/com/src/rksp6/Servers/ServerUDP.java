package com.src.rksp6.Servers;

import com.src.rksp6.object.objShape;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class ServerUDP {
    private DatagramSocket socket;
    private boolean isRunning;
    private byte[] buf;
    String encoding;
    private ServerSerialization mem;

    public ServerUDP(int port, int bufferSize, String _encoding) throws Exception {
        socket = new DatagramSocket(port);
        buf = new byte[bufferSize];
        encoding = _encoding;
        mem = new ServerSerialization();
    }

    public void run(){
        if(!isRunning)
            isRunning = true;
        else
            return;

        while(isRunning) {
            var packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                var data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
                var answer = processRequest(new String(data, encoding), data);

                if(answer == "Server received shape.")
                    continue;

                socket.send(
                        new DatagramPacket(
                                answer.getBytes(),
                                answer.length(),
                                packet.getAddress(),
                                packet.getPort()
                        )
                );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        socket.close();
    }

    private String processRequest(String request, byte[] object){
        System.out.println(request);
        switch (request) {
            case ("SHAPES") -> {
                return mem.getStringShapes();
            }
            case ("NAMES") -> {
                return mem.getNames();
            }
            case ("QUANTITY") -> {
                return Integer.toString(mem.getShapes().size());
            }
            case ("CLEAR") -> {
                mem.clearShapes();
                return "Cleared";
            }
            default -> {
                try {
                    var shape = (objShape)convertFromBytes(object);
                    if(shape != null) {
                        mem.getShapes().add(shape);
                        return "Server received shape.";
                    } else
                        throw new StreamCorruptedException();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        System.out.println("Uknown request" + " " + request);
        return "Uknown request.";
    }

    public static Object convertFromBytes(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String args[]) throws Exception {
        var server = new ServerUDP(4444, 1024, "UTF-8");

        while(true){
            server.run();
        }
    }
}
