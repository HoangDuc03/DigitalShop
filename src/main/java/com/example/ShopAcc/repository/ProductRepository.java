package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    @Query("SELECT COUNT(p) FROM Product p WHERE p.name = :productName AND p.status = true")
    int countByNameAndStatus(@Param("productName") String productName);

    Page<Product> findProductTypeByNameContainingAndPriceBetween(String productname, int minPrice, int maxPrice, Pageable pageable);
    Page<Product> findProductTypeByNameContainingAndPriceGreaterThanEqual(String productname, int minPrice, Pageable pageable);
    Page<Product> findProductTypeByNameContainingAndPriceLessThanEqual(String productname, int maxPrice, Pageable pageable);
    Product findProductByID(int id);


    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.Sold = p.Sold + :quantity WHERE p.ID = :productId")
    void updateSoldQuantity(@Param("productId") int productId, @Param("quantity") int quantity);

    Page<Product> findProductTypeByStatusEqualsAndNameContainingAndPriceBetween(
            boolean status, String name, int price, int price2, Pageable pageable);
    Page<Product> findProductTypeByStatusEqualsAndNameContainingAndPriceGreaterThanEqual(
            boolean status, String name, int price, Pageable pageable);

    Page<Product> findProductTypeByStatusEqualsAndNameContainingAndPriceLessThanEqual(
            boolean status, String name, int price, Pageable pageable);

    List<Product> findByStatus(boolean status);

    Page<Product> findProductByStatusEqualsAndNameContaining(boolean status, String name, Pageable pageable);
}
