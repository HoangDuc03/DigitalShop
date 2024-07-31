package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.CartItem;
import com.example.ShopAcc.repository.CartItemRepository;
import com.example.ShopAcc.repository.ProductDetailRepository;
import com.example.ShopAcc.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public int addToCart(int productId, int userId, int currentAvailable) {
        // Check if the cart item already exists for this user
        CartItem existingCartItem = cartItemRepository.findByProductIdAndUserId(productId, userId);
        if(currentAvailable == 0)
            return 0;

        if (existingCartItem != null) {
            // If it exists, increment the quantity
            if(currentAvailable > existingCartItem.getQuantity()) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            } else {
                existingCartItem.setQuantity(currentAvailable);
            }
            cartItemRepository.save(existingCartItem);
        } else {
            // If it doesn't exist, create a new cart item
            CartItem newCartItem = new CartItem();
            newCartItem.setProductId(productId);
            newCartItem.setUserId(userId);
            newCartItem.setQuantity(1);
            cartItemRepository.save(newCartItem);
        }
        return 1;
    }

    @Override
    public List<CartItem> getAllCartItemsByUserId(int userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public int updateQuantity(int cartItemId, int quantity, int available) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            if (quantity > available) {
                System.out.println("Quantity is greater than available");
                return 1;
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return 0;
        }
        return 2;
    }


    @Override
    public void deleteCartItem(int cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(int userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    public void purchaseSelectedItems(List<Integer> cartItemIds, int userId) {
        // Implementation for purchasing selected cart items
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);
        // Perform the purchase operation
        cartItemRepository.deleteAll(cartItems);
        // Add additional logic for purchase if needed (e.g., update inventory, create order records)
    }


    public List<Integer> getProductDetailIdsToUpdate(List<Integer> selectedItems, List<Integer> quantities) {
        List<Integer> productDetailIdsToUpdate = new ArrayList<>();
        // Logic to fetch and add ProductDetail IDs based on selectedItems and quantities
        // For demonstration, assume you fetch from database or calculate based on selectedItems and quantities
        // Example: fetching first 'quantity' product detail IDs for each 'productId' in selectedItems
        for (int i = 0; i < selectedItems.size(); i++) {
            int productId = selectedItems.get(i);
            int quantity = quantities.get(i);
            List<Integer> productDetailIds = productDetailRepository.findTopNProductDetailId(productId, quantity);
            for(Integer productDetailId : productDetailIds) {
                System.out.println("ProductDetailId: " + productDetailId);
            }
            productDetailIdsToUpdate.addAll(productDetailIds);
        }
        return productDetailIdsToUpdate;
    }
}
