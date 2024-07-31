package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.Purchasehistorydetail;
import com.example.ShopAcc.repository.PurchaseHistoryDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseHistoryDetailServiceImpl {
    @Autowired
    PurchaseHistoryDetailRepo purchaseHistoryDetailRepo;

    public List<Purchasehistorydetail> findLatestBill(int purchaseID) {
        return purchaseHistoryDetailRepo.findLatestBill(purchaseID);
    }
}
