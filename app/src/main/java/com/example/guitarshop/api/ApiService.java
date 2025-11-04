package com.example.guitarshop.api;

import com.example.guitarshop.models.LoginRequest;
import com.example.guitarshop.models.LoginResponse;
import com.example.guitarshop.models.Product;
import com.example.guitarshop.models.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @Headers("Content-Type: application/json")
    @POST("Auth/register")
    Call<Void> register(@Body RegisterRequest request);
    @Headers("Content-Type: application/json")
    @GET("Product/getbycategoryid")
    Call<List<Product>> getProductsByCategory(@Query("categoryId") int categoryId);
}