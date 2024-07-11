package com.example.ShopAcc.service;

import com.example.ShopAcc.dto.ResetPasswordDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;

public interface UserServiceI {
    ResponseEntity<ResponseObject> createUser(User user, HttpSession session);
    ResponseEntity<ResetPasswordDto> sendOtp(String email, HttpSession session);
    ResponseEntity<ResetPasswordDto> verifyOtp(int userId, String otp,HttpSession session);
    ResponseEntity<ResetPasswordDto> resetPassword(int userId, String newPassword, String reNewPassword);
}
