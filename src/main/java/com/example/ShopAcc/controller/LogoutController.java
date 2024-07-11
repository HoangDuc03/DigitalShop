package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

@Controller
public class LogoutController {

    @ModelAttribute("userdto")
    public UserDto userDto() {
        return new UserDto(); // Adjust this based on your requirements
    }

    @GetMapping("/logout")
    public String logout(UserDto userDto, WebRequest request, SessionStatus status, HttpSession session) {
        // Remove specific session attributes
        session.removeAttribute("infor");

        // Mark the session status as complete
        status.setComplete();

        // Remove the model attribute from the request
        request.removeAttribute("userdto", WebRequest.SCOPE_SESSION);

        // Redirect to the login page
        return "redirect:/login"; // Assuming "/login" is the correct endpoint
    }
}
