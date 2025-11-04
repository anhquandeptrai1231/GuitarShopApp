package com.example.guitarshop.models;

import com.google.gson.annotations.SerializedName;

public class AddCartItemRequest {
    @SerializedName("UserId")
    public int userId;

    @SerializedName("ProductId")
    public int productId;

    @SerializedName("Quantity")
    public int quantity;
}
