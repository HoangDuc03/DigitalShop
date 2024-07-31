package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.Wallet;
import com.example.ShopAcc.repository.WalletRepository;
import com.example.ShopAcc.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public boolean checkWallet(int totalPrice, int currentWallet) {
        return totalPrice <= currentWallet;
    }

    @Override
    public int updateWallet(int totalPrice, int currentWallet) {
        if (checkWallet(totalPrice, currentWallet)) {
            return currentWallet - totalPrice;
        }
        return currentWallet;
    }

    @Override
    public Wallet getWalletUser(int userId) {
        System.out.println("Searching for Wallet with AccountId: " + userId);
        for (Wallet wallet : walletRepository.getAllWallet()) {
            System.out.println("Checking Wallet: " + wallet);
            if (wallet.getAccountid().getId() == userId) {
                System.out.println("Found Wallet: " + wallet);
                return wallet;
            }
        }
        System.out.println("No Wallet found for AccountId: " + userId);
        return null;
    }
}
