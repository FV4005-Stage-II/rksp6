package com.src.rksp6.Clients;

import com.src.rksp6.Servers.ServerRequest;

import java.io.File;

public interface IClient {
    void send(String message);
    void send(File file);
    String request(ServerRequest request);
}
