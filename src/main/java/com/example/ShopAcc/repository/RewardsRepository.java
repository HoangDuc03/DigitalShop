package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Rewards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RewardsRepository extends JpaRepository<Rewards,Integer> {
    @Query(value = "select `point` from Users where ID = ?", nativeQuery = true)
    int PointUser(int id);

    @Override
    Page<Rewards> findAll(Pageable pageable);

    @Query(value = "select sum(percent) from Rewards ", nativeQuery = true)
    double SumPercent();
}
