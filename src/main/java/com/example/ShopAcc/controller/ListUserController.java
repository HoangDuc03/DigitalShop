package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Role;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.RoleRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.UserService;
import com.example.ShopAcc.service.hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Controller
public class ListUserController extends hash {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/listuser")
    public String listUsers(Model model,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size) {
        Page<User> userPage = userService.getUsers(keyword, page, size);
        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "listuser";
    }

    @GetMapping("/deleteuser")
    public String deleteUser(@RequestParam("id") int userId) {
        userRepository.deleteById(userId);
        return "redirect:/listuser";
    }

    @GetMapping("/edituser")
    public String editUserForm(@RequestParam("id") int userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("user", user);
        return "edituser";
    }

    @PostMapping("/edituser")
    public String editUser(Model model,
                           @RequestParam("id") int userId,
                           @RequestParam("username") String userName,
                           @RequestParam("fullname") String fullName,
                           @RequestParam("email") String email,
                           @RequestParam("phone") String phoneNumber,
                           @RequestParam("roleid") int roleid,
                           @RequestParam("password") String passwordnew) {

        User user = userRepository.findById(userId).orElse(null);
        user.setUserName(userName);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        Role role = roleRepository.findById(roleid).orElse(null);
        user.setRoleID(role);

        if(!passwordnew.equals(user.getPassword())){
            user.setPassword(hashcode(passwordnew));
        }

        List<User> usersWithSameUserName = userRepository.findByUserNameAndIdNot(userName, userId);
        if (!usersWithSameUserName.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists");
            return "edituser";
        }

        List<User> usersWithSameEmail = userRepository.findByEmailAndIdNot(email, userId);
        if (!usersWithSameEmail.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Email already exists");
            return "edituser";
        }

        userRepository.save(user);
        return "redirect:/listuser";
    }

    @GetMapping("/createuser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "createuser";
    }

    @PostMapping("/createuser")
    public String createUser(Model model,
                             @RequestParam("username") String userName,
                             @RequestParam("password") String password,
                             @RequestParam("fullname") String fullName,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phoneNumber,
                             @RequestParam("roleid") int roleid) {

        User user = new User();
        user.setUserName(userName);
        user.setPassword(hashcode(password));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        Role role = roleRepository.findById(roleid).orElse(null);
        user.setRoleID(role);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedTimestamp = formatter.format(now);
        user.setCreatedAt(formattedTimestamp);

        if (userRepository.findByUserName(userName) != null) {
            model.addAttribute("errorMessage", "Username already exists");
            return "createuser";
        }
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("errorMessage", "Email already exists");
            return "createuser";
        }

        userRepository.save(user);
        return "redirect:/listuser";
    }
}

