package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.model.*;
import com.example.ShopAcc.repository.BlogRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.repository.WalletRepository;
import com.example.ShopAcc.service.EmailService;
import com.example.ShopAcc.service.UserService;
import jakarta.servlet.http.HttpSession;
import com.example.ShopAcc.dto.ResetPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final EmailService emailService;
    @Autowired
    WalletRepository walletRepository;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model, HttpSession session) throws NoSuchAlgorithmException {
        ResponseEntity<ResponseObject> response = userService.createUser(user, session);
        if (response.getStatusCode().is2xxSuccessful()) {
            User data = (User) response.getBody().getData();
            session.setAttribute(data.getEmail(), data);
            if(userRepository.getUserByEmail(data.getEmail()) != null)
            {
                model.addAttribute("EmailExist", "Email already exists");
                return "register";
            }

            model.addAttribute("mail",data.getEmail());
            model.addAttribute("isVerify",false);
            return "verifyOtp";
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
            return "register";
        }
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String mail, @RequestParam(name = "otp",required = false) String otp, Model model, HttpSession session,
                            @RequestParam String action) {
        model.addAttribute("mail",mail);
        model.addAttribute("isVerify",true);
        System.out.println(action);
        if(action.equals("Verify")) {
            if(session.getAttribute("otp")==null){

                model.addAttribute("errorMessage", "OTP expired!!!");
                return "verifyOtp";
            }

            otp checkOTP = (otp)session.getAttribute("otp");
            if (checkOTP.getOtp().equals(otp) && checkOTP.getUser().getEmail().equals(mail) ) {
                User newUser = checkOTP.getUser();

                newUser.setCreatedAt(dateFormatter.format(LocalDate.now()));
                userRepository.save(newUser);

                Wallet newwallet =new Wallet();
                newwallet.setAccountid(newUser);
                newwallet.setWallet(0);
                System.out.println(newwallet.getAccountid().getId());
                walletRepository.save(newwallet);


                session.removeAttribute("otp");
                return "redirect:login";
            } else {
                int again = checkOTP.getAgain();
                if(again<=0)
                {
                    session.removeAttribute("otp");
                    model.addAttribute("errorMessage", "OTP expired!!!");
                    return "verifyOtp";
                }
                model.addAttribute("errorMessage", "Invalid OTP. Please try again.You have "+ again +" retries.");
                model.addAttribute("mail", mail);

                again--;
                checkOTP.setAgain(again);
                session.setAttribute("otp",checkOTP);
                return "verifyOtp";
            }
        }
        else if(action.equals("SendOtp")){
            Random random = new Random();
            int rd = 100000 + random.nextInt(900000);
            String code = String.valueOf(rd);
            User user = (User) session.getAttribute(mail);
            otp verifyOtp = new otp(user,code,3);
            session.setAttribute("otp",verifyOtp);
            emailService.sendOtpEmail(mail, code);
            return "verifyOtp";
        }
        else {
            Random random = new Random();
            int rd = 100000 + random.nextInt(900000);
            String code = String.valueOf(rd);
            User user = (User) session.getAttribute(mail);
            otp verifyOtp = new otp(user,code,3);
            session.setAttribute("otp",verifyOtp);
            emailService.sendOtpEmail(mail, code);
            return "verifyOtp";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
}
