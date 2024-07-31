package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Discount;
import com.example.ShopAcc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Query("SELECT d.id FROM Discount d where d.userId = :userId")
    List<Integer> findDiscountIDbyUserID(@Param("userId") int userId);
    Optional<Discount> findByUserId(int userId);
}
