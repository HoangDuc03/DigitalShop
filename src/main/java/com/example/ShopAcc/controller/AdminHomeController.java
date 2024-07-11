package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/*
- Get user and return to admin_home if user_role is ADMIN
 */

@Controller
@AllArgsConstructor
@SessionAttributes("userdto")
public class AdminHomeController {
    @ModelAttribute("userdto")
    public UserDto userDto(){
        return new UserDto();
    }
    @GetMapping("/admin_home")
    public String showHomeForm(@ModelAttribute("userdto") UserDto userDto, Model model){
        return "/admin_home";
    }
}
