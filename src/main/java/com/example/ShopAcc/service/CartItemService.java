package com.example.ShopAcc.service;

import com.example.ShopAcc.model.CartItem;

import java.util.List;

public interface CartItemService {
    void addToCart(int productId, int userId);
    List<CartItem> getAllCartItemsByUserId(int userId);
    void updateQuantity(int cartItemId, int quantity);
    void deleteCartItem(int cartItemId);
    void clearCart(int userId);
}
