package com.example.ShopAcc.repository;
import com.example.ShopAcc.dto.DiscountDetailDTO;
import com.example.ShopAcc.model.DiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, Integer> {

    @Query("SELECT dt.id FROM DiscountDetail dt where dt.discountID IN :discountId and dt.status = true and dt.quantity > 0")
    List<Integer> findDiscountDetailIDbyDiscountID(@Param("discountId") List<Integer> discountId);

    @Query("SELECT dt.value FROM DiscountDetail dt where dt.id = :detailID")
    double findValueByDiscountDetailID(@Param("detailID") int detailID);

    @Query("SELECT dt.value FROM DiscountDetail dt where dt.id = :detailID")
    String findValueStringByDiscountDetailID(@Param("detailID") int detailID);

    @Query("SELECT dt FROM DiscountDetail dt WHERE dt.id =: detailID")
    DiscountDetail findDiscountDetailByID(@Param("detailID") int detailID);

    void deleteById(int id);

    @Modifying
    @Transactional
    @Query("UPDATE DiscountDetail d SET d.quantity = :quantity, d.value = :value, d.status = :status WHERE d.id = :id")
    void updateDiscountDetail(@Param("id") int id, @Param("quantity") int quantity, @Param("value") String value, @Param("status") boolean status);

    @Modifying
    @Transactional
    @Query("UPDATE DiscountDetail dt SET dt.quantity = dt.quantity - 1 WHERE dt.id = :detailID")
    void updateDiscountQuantity(@Param("detailID") int detailID);

    @Query("SELECT new com.example.ShopAcc.dto.DiscountDetailDTO(d.userId, dd.id, dd.discountID, dd.quantity, dd.value, dd.status) " +
            "FROM DiscountDetail dd JOIN Discount d ON dd.discountID = d.id " +
            "ORDER BY d.userId ASC")
    List<DiscountDetailDTO> findAllWithUserID();
}