package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.WalletHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface WallethistoryRepository extends JpaRepository<WalletHistory, Integer> {

    @Query("SELECT w FROM WalletHistory w WHERE FUNCTION('DATE', w.date) = :date")
    Page<WalletHistory> findAllByDate(@Param("date") Date date, Pageable pageable);

    @Query("SELECT w FROM WalletHistory w")
    Page<WalletHistory> findAll(Pageable pageable);
}