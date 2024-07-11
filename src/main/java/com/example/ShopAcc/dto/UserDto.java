package com.example.ShopAcc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//This Class is the same as User but unease some of information. Shorten the scale of entity constructor
public class UserDto implements Serializable {
    private String email;
    private String password;
    private String userDisplayName;
    private String fullName;
    private boolean captchaWrong;

    public boolean isValid() {
        return email != null && password != null && !captchaWrong;
    }
}
