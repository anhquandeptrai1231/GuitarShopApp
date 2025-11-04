package com.example.guitarshop.models;

public class Category {
    private int id;
    private String name;
    private int imageResId;

    public Category(int id, String name, int imageResId) {
        this.id = id;
        this.name = name;
        this.imageResId = imageResId;
    }
    public int getId() { return id; }
    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}