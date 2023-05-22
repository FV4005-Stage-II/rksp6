package com.src.rksp6.Servers;
import static spark.Spark.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class ServerSpark {
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public ServerSpark() {
        port(8080);
        get("/hello/:name", (request, response) -> {
            return "Hello " + request.params(":name");
        });
    }

    public static void main(String[] args) {
        var server = new ServerSpark();
    }
}
