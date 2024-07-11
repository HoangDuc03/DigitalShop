package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserProfileController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model, @RequestParam int id) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/editprofile")
    public String showEditProfileForm(Model model, @RequestParam int id) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "editprofile";
    }

    @PostMapping("/editprofile")
    public String updateProfile(User user, BindingResult result) {
        if (result.hasErrors()) {
            return "editprofile";
        }
        User user_current = userRepository.findById(user.getId()).orElse(null);
        user.setPassword(user_current != null ? user_current.getPassword() : null);
        user.setRoleID(user_current.getRoleID());
        userRepository.save(user);
        return "redirect:/profile?id=" + user.getId();
    }
}
