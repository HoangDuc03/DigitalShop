package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query(
            value = "select * from Wallet",
            nativeQuery = true)
    List<Wallet> getAllWallet();

//        @Query("SELECT w.Wallet FROM Wallet w WHERE w.Accountid = :userId")
//    int findWalletByAccountId(@Param("userId") int userId);

    @Query(value = "select * from Wallet where Accountid = ?",
    nativeQuery = true)
    Wallet findByAccountid(int accountid);
}
