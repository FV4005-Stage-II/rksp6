package com.src.rksp6.Clients;

import com.src.rksp6.Servers.ServerRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class ClientHttp implements IClient {
    private Retrofit retrofit;
    private static Retro retro;
    public ClientHttp() {
        retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080")
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        retro = retrofit.create(Retro.class);
    }


    @Override
    public void send(String message) {

    }

    @Override
    public void send(File file) {

    }

    @Override
    public void send(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            var response = retro.uploadShape(
                    Base64.getEncoder().encodeToString(bos.toByteArray())
            ).execute();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String request(ServerRequest request) {
        Response<String> response = null;
        try {
            switch (request.toString()) {
                case "SHAPES" -> {
                    response = retro.getShapes().execute();
                }
                case "NAMES" -> {
                    response = retro.getNames().execute();
                }
                case "CLEAR" -> {
                    response = retro.clear().execute();
                }
                case "QUANTITY" -> {
                    response = retro.getQuantity().execute();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return response.body();
    }
}
