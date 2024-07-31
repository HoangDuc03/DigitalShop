package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserId(int userId);
    CartItem findByProductIdAndUserId(int productId, int userId);
}
