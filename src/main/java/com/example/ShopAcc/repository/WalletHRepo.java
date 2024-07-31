package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.WalletH;
import com.example.ShopAcc.model.WalletHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.beans.Transient;

public interface WalletHRepo extends JpaRepository<WalletH,Integer> {
    @Query(value = "select * from Wallethistory where Walletid = ?1 AND `Describe` = ?2",
            nativeQuery = true)
    WalletH findWallet(int wallet, String code);

    @Modifying
    @Transactional
    @Query(value = "insert into wallethistory (Walletid,Money,`date`,`Describe`) values (?1,?2,?3,?4)",
            nativeQuery = true)
    void saveWalletPross(int walletid,int money ,String date,String describe);

    @Modifying
    @Transactional
    @Query(value = "Update wallethistory set `Describe` = ?1 where ID =?2",
            nativeQuery = true)
    void comfirmSuccess(String describe,int id);
}
