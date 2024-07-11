package com.example.ShopAcc.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;

public class hash {

    public String hashcode(String password){

        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String hashpass = encoder.encode(password);
        return hashpass;
    }
    public boolean check(String raw , String encoded){
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        if(encoder.matches( encoded,raw)){
            return true;
        }
        else{
            return false;
        }
    }

}
