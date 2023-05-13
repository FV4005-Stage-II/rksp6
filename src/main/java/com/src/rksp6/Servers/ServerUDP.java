package com.src.rksp6.Servers;

import com.src.rksp6.FileManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ServerUDP extends Thread {
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
        mem.setShapes();
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
                var answer = processRequest(new String(data, encoding));

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

    private String processRequest(String request){
        switch (request) {
            case ("SHAPES") -> {
                return mem.getShapes().toString();
            }
            case ("NAMES") -> {
                return mem.getNames();
            }
            case ("QUANTITY") -> {
                return Integer.toString(mem.getShapes().size());
            }
            case ("CLEAR") -> {
                return "Cleared";
            }
        }
        return "Uknown request.";
    }

    static byte[] trim(byte[] bytes)
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0)
        {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }


    public static void main(String args[]) throws Exception {
        var server = new ServerUDP(4444, 1024, "UTF-8");

        while(true){
            server.run();
        }
    }
}
