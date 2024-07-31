package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.repository.PurchaseHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseHistoryServiceImpl {
    @Autowired
    PurchaseHistoryRepo purchaseHistoryRepo;

    public int findLatesID() {
        return purchaseHistoryRepo.findLatestID();
    }
}
