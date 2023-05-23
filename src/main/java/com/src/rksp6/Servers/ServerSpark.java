package com.src.rksp6.Servers;
import static spark.Spark.*;

public class ServerSpark {
    ServerMemory memory;
    public ServerSpark() {
        memory = new ServerMemory();
        port(8080);
        get("/hello/names", (request, response) -> {
            return "Hello " + request.params(":name");
        });
    }

    public static void main(String[] args) {
        var server = new ServerSpark();
    }
}
