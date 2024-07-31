package com.example.ShopAcc.service;

import com.example.ShopAcc.model.Cart;

import java.util.List;

public interface CartService {
    void addToCart(int productId, int userId);

    List<Cart> getAllCartItemsByUserId(int userId);

    void updateQuantity(int cartItemId, int quantity);
    void deleteCartItem(int cartItemId);
    void clearCart(int userId);

}