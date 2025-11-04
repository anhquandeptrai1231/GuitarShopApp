package com.example.guitarshop.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("Username")
    private String username;

    @SerializedName("Password")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}