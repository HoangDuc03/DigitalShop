package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<Product, Integer> {

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findProductTypeByNameContainingAndPriceBetween(String productname, int minPrice, int maxPrice, Pageable pageable);
    Page<Product> findProductTypeByNameContainingAndPriceGreaterThanEqual(String productname, int minPrice, Pageable pageable);
    Page<Product> findProductTypeByNameContainingAndPriceLessThanEqual(String productname, int maxPrice, Pageable pageable);
    }
}
