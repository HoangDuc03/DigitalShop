package com.example.ShopAcc.service;

import com.example.ShopAcc.dto.ResetPasswordDto;
import org.springframework.http.ResponseEntity;

public interface UserServiceImpl {
    ResponseEntity<ResetPasswordDto> sendOtp(String email);
    ResponseEntity<ResetPasswordDto> verifyOtp(int userId, String otp);
    ResponseEntity<ResetPasswordDto> resetPassword(int userId, String newPassword, String reNewPassword);
}
