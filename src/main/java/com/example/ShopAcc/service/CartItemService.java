package com.example.ShopAcc.service;

import com.example.ShopAcc.model.CartItem;

import java.util.List;


public interface CartItemService {
    int addToCart(int productId, int userId, int currentAvailable);
    List<CartItem> getAllCartItemsByUserId(int userId);
    int updateQuantity(int cartItemId, int quantity, int available);
    void deleteCartItem(int cartItemId);
    void clearCart(int userId);
    void purchaseSelectedItems(List<Integer> cartItemIds, int userId);
    List<Integer> getProductDetailIdsToUpdate(List<Integer> selectedItems, List<Integer> quantities);
}