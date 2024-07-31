package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Discount;
import com.example.ShopAcc.model.Role;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.model.Wallet;
import com.example.ShopAcc.repository.DiscountRepository;
import com.example.ShopAcc.repository.RoleRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.repository.WalletRepository;
import com.example.ShopAcc.service.UserService;
import com.example.ShopAcc.service.WalletService;
import com.example.ShopAcc.service.hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
@RequestMapping("/admin")
@Controller
public class ListUserController extends hash {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private DiscountRepository discountRepository;
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

    @GetMapping("/togglestatus")
    public String deleteUser(@RequestParam("id") int userId) {
        User user = userRepository.findById(userId).orElse(null);
        user.setStatus(!user.isStatus());
        userRepository.save(user);
        return "redirect:/admin/listuser";
    }

    @GetMapping("/edituser")
    public String editUserForm(@RequestParam("id") int userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("user", user);
        Wallet wallet = walletRepository.findByAccountid(userId);
        model.addAttribute("wallet",wallet);
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
                           @RequestParam("password") String passwordnew,
                           @RequestParam("wallet") int wallet) {

        User user = userRepository.findById(userId).orElse(null);
        user.setUserName(userName);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        Role role = roleRepository.findById(roleid).orElse(null);
        user.setRoleID(role);

        Wallet walletuser = walletRepository.findByAccountid(userId);
        walletuser.setWallet(wallet);

        if(!passwordnew.equals(user.getPassword())){
            user.setPassword(hashcode(passwordnew));
        }

        List<User> usersWithSameUserName = userRepository.findByUserNameAndIdNot(userName, userId);
        if (!usersWithSameUserName.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("wallet",walletuser);
            model.addAttribute("errorMessage", "Username already exists");
            return "edituser";
        }

        List<User> usersWithSameEmail = userRepository.findByEmailAndIdNot(email, userId);
        if (!usersWithSameEmail.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("wallet",walletuser);
            model.addAttribute("errorMessage", "Email already exists");
            return "edituser";
        }

        userRepository.save(user);
        walletRepository.save(walletuser);
        return "redirect:/admin/listuser";
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
        user.setPassword(hashcode(password));
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setUserName(userName);
        Role role = roleRepository.findById(roleid).orElse(null);
        user.setRoleID(role);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedTimestamp = formatter.format(now);
        user.setCreatedAt(formattedTimestamp);

        if (userRepository.findByUserName(userName) != null) {
            user.setUserName("");
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Username already exists");
            return "createuser";
        }
        if (userRepository.findByEmail(email) != null) {
            user.setEmail("");
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Email already exists");
            return "createuser";
        }

        userRepository.save(user);

        Wallet newwallet =new Wallet();
        newwallet.setAccountid(user);
        newwallet.setWallet(0);
        System.out.println(newwallet.getAccountid().getId());
        walletRepository.save(newwallet);

        return "redirect:/admin/listuser";
    }
}

