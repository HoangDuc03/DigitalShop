package com.example.ShopAcc.service;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.User;

import java.security.NoSuchAlgorithmException;

public interface UserServiceQ {
    void save(UserDto userDto);
    Boolean checkPasswordUser(String email, String password) throws NoSuchAlgorithmException;
    Boolean checkUserByEmail(String email);
    User getUserByEmail(String email);
}

