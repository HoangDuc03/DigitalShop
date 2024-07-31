package com.example.ShopAcc.service;

import com.example.ShopAcc.model.Wallet;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    boolean checkWallet(int totalPrice, int currentWallet);
    int updateWallet(int totalPrice, int currentWallet);
    Wallet getWalletUser(int userId);
}
