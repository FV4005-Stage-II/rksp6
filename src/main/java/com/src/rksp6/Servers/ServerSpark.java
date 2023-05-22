package com.src.rksp6.Servers;
import static spark.Spark.*;
public class ServerSpark {
    public ServerSpark() {
        get("/hello", (req, res) -> "Hello World");
    }

}
