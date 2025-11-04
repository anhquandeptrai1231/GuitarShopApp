package com.example.guitarshop;

import com.example.guitarshop.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartSingleton {
    private static CartSingleton instance;
    private List<CartItem> cartItems;

    private CartSingleton() {
        cartItems = new ArrayList<>();
    }

    public static CartSingleton getInstance() {
        if (instance == null) {
            instance = new CartSingleton();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(CartItem item) {
        // nếu đã có sản phẩm rồi thì cộng số lượng
        for (CartItem cartItem : cartItems) {
            if (cartItem.productId == item.productId) { // so sánh trực tiếp bằng productId
                cartItem.quantity += item.quantity;    // cộng số lượng
                return;
            }
        }
        cartItems.add(item);
    }
}