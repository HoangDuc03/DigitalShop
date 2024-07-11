package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChangePassController extends hash {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/changepassword")
    public String changepassword(Model model, @RequestParam int id) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "changepass";
    }
    @PostMapping("/changepassword")
    public String changepassword(@RequestParam int id,
                                 String currentPassword,
                                 String newPassword,
                                 RedirectAttributes redirectAttributes) {
        // getpassword => database
        // currentPassword => password input
        // newpassword => new password input
        System.out.println(id+ " "+currentPassword+" "+newPassword);
        User user = userRepository.findById(id).orElse(null);
        if (user == null || !check( user.getPassword(),currentPassword)) {
            redirectAttributes.addFlashAttribute("Message", "Current password is incorrect.");
            return "redirect:/changepassword?id=" + id;
        }

        user.setPassword(hashcode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("Message", "Password changed successfully.");
        return "redirect:/changepassword?id=" + id;
    }
}
