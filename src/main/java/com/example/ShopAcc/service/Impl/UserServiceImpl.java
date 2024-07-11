package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.UserService;
import com.example.ShopAcc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
//Implement an interface to reduce the possibility of losing source
public class UserServiceImpl extends hash implements UserServiceQ {
    //using repository to compute with database
    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(UserDto userDto) {
        // Implement saving user logic here
    }

    //Check if email is existed in dtb, and does pass match with email
    @Override
    public Boolean checkPasswordUser(String email, String password){
        User user = userRepository.findByEmail(email);
        //System.out.println(password+" : "+user.getPassword());
        return user != null && check(user.getPassword(),password);
    }

    //Check if email is existed in dtb
    @Override
    public Boolean checkUserByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    //Get user with matched email
    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        System.out.println("User FullName from DB: " + user.getFullName()); // Thêm log để kiểm tra giá trị fullName
        return user;
    }
}
