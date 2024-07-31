package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT p.ID FROM Product p JOIN ProductDetail pd ON p.ID = pd.productid WHERE pd.id = :productDetailId")
    Integer findProductIdByProductDetailId(@Param("productDetailId") int productDetailId);

    @Query("SELECT COUNT(p) FROM ProductDetail p WHERE p.productid = :productId AND p.status = true")
    int countByIDAndStatus(@Param("productId") int productId);

    @Query(value = "SELECT pd.id FROM ProductDetail pd WHERE pd.productid = :productId AND pd.status = true LIMIT :quantity", nativeQuery = true)
    List<Integer> findTopNProductDetailId(@Param("productId") int productId, @Param("quantity") int quantity);

    @Modifying
    @Transactional
    @Query("UPDATE ProductDetail pd SET pd.status = false WHERE pd.id IN :ids")
    void updateProductStatus(@Param("ids") List<Integer> ids);

    

    Page<ProductDetail> findByProductid(int productid, Pageable pageable);

    ProductDetail findBySeri(String seri);

}
