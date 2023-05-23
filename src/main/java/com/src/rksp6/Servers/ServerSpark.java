package com.src.rksp6.Servers;
import com.src.rksp6.object.Conveyor;


import static spark.Spark.*;

public class ServerSpark {
    ServerMemory memory;
    public ServerSpark() {
        memory = new ServerMemory();

        var conveyor = new Conveyor();
        memory.addShape(conveyor.createShape("Circle", 1, 1));
        memory.addShape(conveyor.createShape("Text", 2, 2));



        port(8080);

        get("/NAMES", (request, response) -> {
            return memory.getNames();
        });

        get("/QUANTITY", (request, response) -> {
            return memory.getShapes().size();
        });

        get("/SHAPES", (request, response) -> {
            return memory.getStringShapes();
        });

        get("/CLEAR", (request, response)->{
            memory.clearShapes();
            return "Shapes cleared.";
        });
    }

    public static void main(String[] args) {
        var server = new ServerSpark();
    }
}
