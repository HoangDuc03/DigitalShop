package com.example.ShopAcc.service;

import org.springframework.stereotype.Service;

@Service
public interface CaptchaService {
    String CreateCaptchaForID(int ID);
    int CreateCaptchaID();
    boolean VerifyCaptcha(String Captcha, int ID, String input);
}
