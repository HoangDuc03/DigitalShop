package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.ResetPasswordDto;
import com.example.ShopAcc.model.otp;
import com.example.ShopAcc.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class ForgotPasswordController {

    private final UserService userService;
    @GetMapping("/forgot")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgot")
    public String sendOtp(@RequestParam String email, Model model, HttpSession session) {
        boolean emailExists = userService.checkEmailExists(email);
        if (!emailExists) {
            model.addAttribute("emailNotFound", true);
            return "forgotPassword";
        }
        ResponseEntity<ResetPasswordDto> response = userService.sendOtp(email, session);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("mail", email);
            model.addAttribute("successMessage", "OTP has been sent to your email.");
            return "verifyotpresetpass";
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
            return "forgotPassword";
        }
    }

    @PostMapping("/resendOtp")
    public String resendOtp(@RequestParam String mail, Model model, HttpSession session) {
        ResponseEntity<ResetPasswordDto> response = userService.sendOtp(mail, session);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("mail", mail);
            model.addAttribute("successMessage", "New OTP has been sent to your email.");
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
        }
        return "verifyotpresetpass";
    }

    @PostMapping("/verifyotpresetpass")
    public String verifyOtpResetPass(@RequestParam String otp, @RequestParam String mail, Model model, HttpSession session) {
        otp otpResetPass = (otp) session.getAttribute("otpresetpass");
        if (otpResetPass == null || otpResetPass.getAgain() <= 0) {
            model.addAttribute("expiredMessage", "OTP expired. Please resend OTP.");
            model.addAttribute("mail", mail);
            return "verifyotpresetpass";
        }
        int remainingAttempts = otpResetPass.getAgain();
        if (remainingAttempts <= 0) {
            model.addAttribute("expiredMessage", "OTP expired. Please resend OTP.");
            model.addAttribute("mail", mail);
            session.removeAttribute("otpresetpass");
            return "verifyotpresetpass";
        }
        ResponseEntity<ResetPasswordDto> response = userService.verifyOtp(otp, session);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/resetPassword";
        } else {
            remainingAttempts--;
            otpResetPass.setAgain(remainingAttempts);
            session.setAttribute("otpresetpass", otpResetPass);
            if (remainingAttempts <= 0) {
                model.addAttribute("expiredMessage", "OTP expired. Please resend OTP.");
                model.addAttribute("mail", mail);
            } else {
                model.addAttribute("errorMessage", "Invalid OTP. Please try again");
                model.addAttribute("mail", mail);
            }
            return "verifyotpresetpass";
        }
    }


    @GetMapping("/resetPassword")
    public String showResetPasswordForm() {
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String newPassword, @RequestParam String reNewPassword, Model model, HttpSession session) {
        ResponseEntity<ResetPasswordDto> response = userService.resetPassword(newPassword, reNewPassword, session);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/success";
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
            return "resetPassword";
        }
    }
}
