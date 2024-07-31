package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query(
            value = "select * from Cart where Accountid = ?",
            nativeQuery = true
    )
    List<Cart> findByUserId(int userId);
    @Query(
            value = "select * from Cart where Productid = ?1 and Accountid = ?2",
            nativeQuery = true
    )
    Cart findByProductIdAndUserId(int productId, int userId);
}
