package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.RewardsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RewardsListRepository extends JpaRepository<RewardsList,Integer> {
    @Query(value="select * from RewardsList where Accountid = ?",nativeQuery = true)
    List<RewardsList> rewardsList(int userid);
}
