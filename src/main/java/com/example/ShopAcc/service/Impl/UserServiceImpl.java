package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//Implement an interface to reduce the possibility of losing source
public class UserServiceImpl extends hash implements UserServiceQ {
    @Autowired
    private UserRepository userReponsitory;

    @Override
    public Boolean checkPasswordUser(String email, String password) {
        User user = userReponsitory.findUserByEmail(email);
        if (check( user.getPassword(),password)) return true;
        return false;
    }

    @Override
    public Boolean checkUserbyEmail(String email) {
        User user = userReponsitory.findUserByEmail(email);
        if(user==null) return false;
        return true;
    }

    @Override
    public User getUserbyEmail(String email) {
        return userReponsitory.getUserByEmail(email);
    }

}
