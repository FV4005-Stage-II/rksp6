package com.src.rksp6.Clients;

import com.src.rksp6.Servers.ServerRequest;

public interface IClient {
    void send(String message);
    String request(ServerRequest request);
}
