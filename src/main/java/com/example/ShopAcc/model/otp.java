package com.example.ShopAcc.model;

public class otp {
    private User user;
    private String otp;
    private int again;

    public otp(User user, String otp, int again) {
        this.user = user;
        this.otp = otp;
        this.again = again;
    }

    public otp() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getAgain() {
        return again;
    }

    public void setAgain(int again) {
        this.again = again;
    }
}
