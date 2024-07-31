
package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.hash;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
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
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        // getpassword => database
        // currentPassword => password input
        // newpassword => new password input
        User user = userRepository.findById(id).orElse(null);
        if (user == null || !check( user.getPassword(),currentPassword)) {
            redirectAttributes.addFlashAttribute("Message", "Current password is incorrect.");
            return "redirect:/profile/changepassword?id=" + id;
        }

        user.setPassword(hashcode(newPassword));
        userRepository.save(user);
        if(session.getAttribute("infor") != null)
            session.removeAttribute("infor");
//        redirectAttributes.addFlashAttribute("Message", "Password changed successfully.");
        return "redirect:/login";
    }
}

