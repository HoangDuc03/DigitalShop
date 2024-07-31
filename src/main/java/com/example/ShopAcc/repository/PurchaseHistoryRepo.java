
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseHistoryRepo extends JpaRepository<Purchasehistory, Integer> {

@Query("SELECT p FROM Purchasehistory p WHERE p.product.name LIKE %:productName%")
    Page<Purchasehistory> findAllByProductNameContaining(String productName, Pageable pageable);

    @Query("SELECT p FROM Purchasehistory p WHERE p.product.name LIKE %:productName% AND DATE(p.purchasedate) = DATE(:date)")
    Page<Purchasehistory> findAllByProductNameContainingAndPurchasedate(String productName, Date date, Pageable pageable);

    @Query(value = "SELECT * FROM Purchasehistory WHERE Accountid = ?1", nativeQuery = true)
    Page<Purchasehistory> findAllByAccountid(int userid, Pageable p);

    @Query(
            value ="SELECT Purchasehistory.*,Product.`Name` FROM purchasehistory\n" +
                    "                    JOIN Product ON purchasehistory.productid = Product.ID\n" +
                    "                    WHERE Product.`Name` like %?1% AND purchasehistory.Accountid = ?2\n" +
                    "                    order by purchasehistory.`Date` desc;",
            nativeQuery = true)
    Page<Purchasehistory> findAllByName(String name, int id, Pageable p);

    @Query("SELECT p.ID FROM Purchasehistory p ORDER BY p.ID DESC LIMIT 1")
    int findLatestID();

    @Query(value = "select Sum(price) from Purchasehistory\n" +
            "WHERE YEAR(`Date`) = YEAR(CURRENT_DATE())\n" +
            "AND MONTH(`Date`) = MONTH(CURRENT_DATE());", nativeQuery = true)
    int Earning();

    @Query(value = "select Sum(price) from Purchasehistory\n" +
            "WHERE YEAR(`Date`) = YEAR(CURRENT_DATE())", nativeQuery = true)
    int EarningInYear();

    @Query(value =  "select Count(*) from Purchasehistory\n" +
                    "join purchasehistorydetail on Purchasehistoryid = Purchasehistory.ID;", nativeQuery = true)
    int AllpurchaseProduct();

    @Query(value =  "select Count(*) from Purchasehistory\n" +
            "join purchasehistorydetail on Purchasehistoryid = Purchasehistory.ID\n" +
            "WHERE YEAR(`Date`) = YEAR(CURRENT_DATE())\n" +
            "AND MONTH(`Date`) = MONTH(CURRENT_DATE());", nativeQuery = true)
    int PurchaseProductInMonth();

    @Query(value =  "select Count(*) from Purchasehistory\n" +
            "join purchasehistorydetail on Purchasehistoryid = Purchasehistory.ID\n" +
            "WHERE YEAR(`Date`) = YEAR(CURRENT_DATE())", nativeQuery = true)
    int PurchaseProductInYear();

    @Query(value =  "select Sum(price) from Purchasehistory\n" +
            "WHERE YEAR(`Date`) = YEAR(CURRENT_DATE())\n" +
            "AND MONTH(`Date`) = ?", nativeQuery = true)
    Optional<Integer> PurchaseProductInMonth(int month);
}

