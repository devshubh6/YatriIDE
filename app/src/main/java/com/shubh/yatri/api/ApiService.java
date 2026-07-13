package com.shubh.yatri.api;

import retrofit2.Call;
import retrofit2.http.*;
import com.google.gson.JsonObject;

public interface ApiService {
    
    // Authentication
    @POST("auth/register")
    Call<JsonObject> register(@Body JsonObject body);

    @POST("auth/login")
    Call<JsonObject> login(@Body JsonObject body);

    @GET("auth/me")
    Call<JsonObject> getCurrentUser();

    // Code Management
    @POST("code/save")
    Call<JsonObject> saveCode(@Body JsonObject body);

    @GET("code/all")
    Call<JsonObject> getUserCodes();

    @GET("code/{id}")
    Call<JsonObject> getCode(@Path("id") String id);

    @DELETE("code/{id}")
    Call<JsonObject> deleteCode(@Path("id") String id);

    // Compilation
    @POST("code/compile")
    Call<JsonObject> compileCode(@Body JsonObject body);
}
