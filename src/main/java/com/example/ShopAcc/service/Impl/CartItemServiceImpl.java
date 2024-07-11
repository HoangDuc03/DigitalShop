package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.CartItem;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.CartItemRepository;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void addToCart(int productId, int userId) {
        // Check if the cart item already exists for this user
        CartItem existingCartItem = cartItemRepository.findByProductIdAndUserId(productId, userId);
        if (existingCartItem != null) {
            // If it exists, increment the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemRepository.save(existingCartItem);
        } else {
            // If it doesn't exist, create a new cart item
            CartItem newCartItem = new CartItem();
            Product product = productRepository.findById(productId).orElse(null);
            User user = userRepository.findById(userId).orElse(null);
            newCartItem.setProduct(product);
            newCartItem.setUser(user);
            newCartItem.setQuantity(1);
            cartItemRepository.save(newCartItem);
        }
    }

    @Override
    public List<CartItem> getAllCartItemsByUserId(int userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void updateQuantity(int cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
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
}
