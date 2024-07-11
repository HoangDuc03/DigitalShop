package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findProductTypeByStatusEqualsAndNameContainingAndPriceBetween(
            boolean status, String name, int price, int price2, Pageable pageable);

    Page<Product> findProductTypeByStatusEqualsAndNameContainingAndPriceGreaterThanEqual(
            boolean status, String name, int price, Pageable pageable);

    Page<Product> findProductTypeByStatusEqualsAndNameContainingAndPriceLessThanEqual(
            boolean status, String name, int price, Pageable pageable);

    List<Product> findByStatus(boolean status);
    Page<Product> findProductTypeByStatusEqualsAndNameContaining(boolean status, String name, Pageable pageable);
    Product findProductByID(int ID);
}
