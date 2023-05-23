package com.src.rksp6.Clients;

import com.src.rksp6.Servers.ServerRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ClientHttp implements IClient {

    public ClientHttp() throws IOException {
        String urlAdress = "localhost:8080/hello/Тесак_Ты_был_добряк";
        URLConnection urlConnection = null;
        URL url = null;
        InputStreamReader isR = null;
        BufferedReader bfR = null;
        try {
            url = new URL(urlAdress);
            urlConnection = url.openConnection();
            isR = new InputStreamReader(urlConnection.getInputStream());
            bfR = new BufferedReader(isR);
            System.out.println(bfR.readLine());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            isR.close();
            bfR.close();
        }
    }
    @Override
    public void send(String message) {

    }

    @Override
    public void send(File file) {

    }

    @Override
    public void send(Object object) {

    }

    @Override
    public String request(ServerRequest request) {
        return null;
    }
}
