package com.example.ShopAcc.service;

import com.example.ShopAcc.model.User;


public interface UserServiceQ {
    Boolean checkPasswordUser(String email,String password);
    Boolean checkUserbyEmail(String email);
    User getUserbyEmail(String email);
}

