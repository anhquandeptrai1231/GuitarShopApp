package com.example.guitarshop.api;

import com.example.guitarshop.models.AddCartItemRequest;
import com.example.guitarshop.models.CartItem;
import com.example.guitarshop.models.LoginRequest;
import com.example.guitarshop.models.LoginResponse;
import com.example.guitarshop.models.Product;
import com.example.guitarshop.models.RegisterRequest;
import com.example.guitarshop.models.UpdateCartItemRequest;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    @GET("cart/{userId}")
    Call<ApiResponse<List<CartItem>>> getCart(@Path("userId") int userId);

    @POST("cart/add")
    Call<Void> addToCart(@Body AddCartItemRequest request);

    @PUT("cart/update")
    Call<Void> updateCartItem(@Body UpdateCartItemRequest request);

    @DELETE("cart/remove/{cartItemId}")
    Call<Void> removeCartItem(@Path("cartItemId") int cartItemId);
    @POST("Ai/chat")
    Call<HashMap<String, String>> chatWithAI(@Body HashMap<String, Object> body);
}