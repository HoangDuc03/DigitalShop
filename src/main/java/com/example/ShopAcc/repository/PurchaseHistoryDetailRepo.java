package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Purchasehistory;
import com.example.ShopAcc.model.Purchasehistorydetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryDetailRepo extends JpaRepository<Purchasehistorydetail, Integer> {
    @Query(
            value = "SELECT * FROM purchasehistorydetail where Purchasehistoryid=?",
            nativeQuery = true)
    List<Purchasehistorydetail> findAllById(int id);

    @Query(
            value = "SELECT COUNT(*)*pt.price AS count\n" +
                    "FROM PurchaseHistoryDetail \n" +
                    "JOIN PurchaseHistory AS ph ON PurchaseHistoryDetail.purchasehistoryid = ph.id\n" +
                    "Join ProductType AS pt ON ph.type = pt.id\n" +
                    "WHERE PurchaseHistoryDetail.purchasehistoryid = ?",
            nativeQuery = true)
    int price(int id);

}
