package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.service.CaptchaService;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    private static final String CHARACTERS = "abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int DEFAULT_CAPTCHA_LENGTH = 5;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String CreateCaptchaForID(int ID) {
        System.out.println("Create Captcha for ID: " + ID);
        StringBuilder captcha = new StringBuilder();

        // Build a random CAPTCHA string of default length
        for (int i = 0; i < DEFAULT_CAPTCHA_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            captcha.append(CHARACTERS.charAt(index));
        }
        System.out.println("Captcha created: " + captcha);
        return captcha.toString();
    }

    @Override
    public int CreateCaptchaID() {
        return (int) (Math.random() * 10000);
    }

    @Override
    public boolean VerifyCaptcha(String Captcha, int ID, String inputCaptcha) {
        System.out.println("Verify Captcha: " + Captcha + " For ID: " + ID + " With input captcha: " + inputCaptcha);
        return Captcha.equals(inputCaptcha);
    }
}
