package com.src.rksp6.Servers;
import com.src.rksp6.object.Conveyor;
import com.src.rksp6.object.objShape;


import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Base64;

import static spark.Spark.*;

public class ServerSpark {
    ServerMemory memory;
    public ServerSpark() {
        memory = new ServerMemory();

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

        put("/SHAPE", (request, response) -> {
            var bytes = Base64.getDecoder().decode(request.body());
            var shape = (objShape)convertFromBytes(bytes);
            memory.addShape(shape);
            return "Shape received";
        });
    }

    public static Object convertFromBytes(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        var server = new ServerSpark();
    }
}
