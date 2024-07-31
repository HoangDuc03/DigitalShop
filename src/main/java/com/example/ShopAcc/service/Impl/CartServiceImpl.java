package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.Cart;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.CartRepository;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void addToCart(int productId, int userId) {
        // Check if the cart item already exists for this user
        Cart existingCart = cartRepository.findByProductIdAndUserId(productId, userId);
        if (existingCart != null) {
            // If it exists, increment the quantity
            existingCart.setQuantity(existingCart.getQuantity() + 1);
            cartRepository.save(existingCart);
        } else {
            // If it doesn't exist, create a new cart item
            Cart newCart = new Cart();
            Product product = productRepository.findById(productId).orElse(null);
            User user = userRepository.findById(userId).orElse(null);
            newCart.setProduct(product);
            newCart.setUser(user);
            newCart.setQuantity(1);
            cartRepository.save(newCart);
        }
    }

    @Override
    public List<Cart> getAllCartItemsByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void updateQuantity(int cartItemId, int quantity) {
        Cart cart = cartRepository.findById(cartItemId).orElse(null);
        if (cart != null) {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(int userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(carts);
    }
}
