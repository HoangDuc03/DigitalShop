package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    ProductDetail findById(int id);
    @Query(
            value = "SELECT * FROM productdetail",
            nativeQuery = true)
    List<ProductDetail> findAllProductDetail();
    //ProductDetail findByWarehouseID(int warehouseID);

    @Query(
    value = "Select count(*) from ProductDetail where id = ?",
    nativeQuery = true)
    int findQuantyti(int ID);
}
