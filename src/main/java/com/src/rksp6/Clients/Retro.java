package com.src.rksp6.Clients;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import spark.Request;


public interface Retro {
    @GET("/NAMES")
    Call<String> getNames();

    @GET("/SHAPES")
    Call<String> getShapes();

    @GET("/QUANTITY")
    Call<String> getQuantity();

    @GET("/CLEAR")
    Call<String> clear();

    @PUT("/SHAPE")
    Call<String> uploadShape(@Body String shape);
}
