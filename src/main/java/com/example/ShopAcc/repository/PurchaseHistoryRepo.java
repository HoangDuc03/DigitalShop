package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Purchasehistory;
import com.example.ShopAcc.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepo extends JpaRepository<Purchasehistory, Integer> {
    @Query(
            value ="SELECT * from purchasehistory\n" +
                    "WHERE Accountid = ?",
            nativeQuery = true)
    Page<Purchasehistory> findAllByAccountid(int userid,Pageable p);

    @Query(
            value ="SELECT purchasehistory.*,Product.`Name` FROM purchasehistory\n" +
                    "                    JOIN Product ON purchasehistory.productid = Product.ID\n" +
                    "                    WHERE Product.`Name` like %?1% AND purchasehistory.Accountid = ?2\n" +
                    "                    order by purchasehistory.`Date` desc;",
            nativeQuery = true)
    Page<Purchasehistory> findAllByName(String name, int id, Pageable p);


}
