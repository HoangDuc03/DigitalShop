package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.CartItem;
import com.example.ShopAcc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query(
            value = "select * from Cart where Accountid = ?",
            nativeQuery = true
    )
    List<CartItem> findByUserId(int userId);
    @Query(
            value = "select * from Cart where Productid = ?1 and Accountid = ?2",
            nativeQuery = true
    )
    CartItem findByProductIdAndUserId(int productId, int userId);
}
