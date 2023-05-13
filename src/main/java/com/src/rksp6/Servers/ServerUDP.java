package com.src.rksp6.Servers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP extends Thread {
    private DatagramSocket socket;
    private boolean isRunning;
    private byte[] buf;
    String encoding;

    public ServerUDP(int port, int bufferSize, String _encoding) throws Exception {
        socket = new DatagramSocket(port);
        buf = new byte[bufferSize];
        encoding = _encoding;
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
            } catch (Exception ex){
                ex.printStackTrace();
                isRunning = false;
                socket.close();
            }

            var adress = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, adress, port);
            try {

                var received = new String(packet.getData(), 0, packet.getLength(), encoding);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            try {
                socket.send(packet);
            } catch (Exception ex){
                ex.printStackTrace();
                isRunning = false;
                socket.close();
            }
        }

        socket.close();
    }

    public static void main(String args[]) throws Exception {
        var server = new ServerUDP(4444, 1024, "UTF-8");

        while(true){
            server.run();
        }
    }
}
