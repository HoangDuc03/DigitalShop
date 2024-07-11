package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.model.Blog;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.model.otp;
import com.example.ShopAcc.repository.BlogRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.EmailService;
import com.example.ShopAcc.service.UserService;
import jakarta.servlet.http.HttpSession;
import com.example.ShopAcc.dto.ResetPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
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
    private final BlogRepository blogRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final EmailService emailService;
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
        if(action.equals("verifyOtp")) {
            otp checkOTP = (otp)session.getAttribute("otp");
            if (checkOTP.getOtp().equals(otp) && checkOTP.getUser().getEmail().equals(mail)) {
                User newUser = checkOTP.getUser();
                userRepository.save(newUser);
                session.removeAttribute("otp");
                return "redirect:login";
            } else {
                int again = checkOTP.getAgain();
                if(again<=0)
                {
                    session.removeAttribute("otp");
                    return "otpreject";
                }
                model.addAttribute("errorMessage", "Invalid OTP. Please try again.");
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


    @GetMapping("/forgot")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "forgotPassword";
    }

    @PostMapping("/forgot")
    public String sendOtp(@RequestParam String email, Model model,HttpSession session) {
        ResponseEntity<ResetPasswordDto> response = userService.sendOtp(email,session);
        if (response.getStatusCode().is2xxSuccessful()) {
            User user = (User) response.getBody().getData();
            model.addAttribute("userId", user.getId());
            return "verifyotpresetpass"; // Redirect to verifyOtp with userId
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
            return "forgotPassword";
        }
    }

    @PostMapping("/verifyotpresetpass")
    public String verifyOtpresetpass(@RequestParam int userId, @RequestParam String otp, Model model,HttpSession session) {

        ResponseEntity<ResetPasswordDto> response = userService.verifyOtp(userId, otp,session);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("userId", userId);
            return "redirect:resetPassword?userId=" + userId; // Redirect to resetPassword with userId
        } else {

            otp otpresetpass = (otp)session.getAttribute("otpresetpass");
            int again = otpresetpass.getAgain() -1 ;
            if(again <= 0)
            {
                session.removeAttribute("otpresetpass");
                return "otpreject";
            }
            otpresetpass.setAgain(again);
            session.setAttribute("otpresetpass",otpresetpass);

            model.addAttribute("errorMessage", response.getBody().getMessage());
            model.addAttribute("userId", userId);
            return "verifyotpresetpass";
        }
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam int userId, Model model) {
        model.addAttribute("userId", userId);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam int userId, @RequestParam String newPassword, @RequestParam String reNewPassword, Model model) {
        ResponseEntity<ResetPasswordDto> response = userService.resetPassword(userId, newPassword, reNewPassword);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:success"; // Redirect to success page
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
            model.addAttribute("userId", userId);
            return "resetPassword";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }

    @GetMapping("/display")
    public String showDisplayAllBlog(Model model) {
        List<Blog> blogs = blogRepository.findAll();
        model.addAttribute("blogs", blogs);
        return "displayAllBlog";
    }

    @GetMapping("/detailBlog/{id}")
    public String showBlogDetails(@PathVariable int id, Model model) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            model.addAttribute("blog", blog);
            return "detailBlog";
        } else {
            return "redirect:/display";
        }
    }

    @GetMapping("/searchBlog")
    public String searchBlog(@RequestParam String searchType,
                             @RequestParam String searchKeyword,
                             Model model) {
        List<Blog> blogs = null;
        try {
            switch (searchType) {
                case "name":
                    blogs = blogRepository.findByNameContaining(searchKeyword);
                    break;
                case "createdAt":
                    LocalDate createdAt = LocalDate.parse(searchKeyword, dateFormatter);
                    blogs = blogRepository.findByCreatedAt(
                            Date.from(createdAt.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    break;
                case "updatedAt":
                    LocalDate updatedAt = LocalDate.parse(searchKeyword, dateFormatter);
                    blogs = blogRepository.findByCreatedAt(
                            Date.from(updatedAt.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    break;
                default:
                    model.addAttribute("infoMessage", "Loại tìm kiếm không hợp lệ.");
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("infoMessage", "Ngày không hợp lệ. Vui lòng nhập ngày theo định dạng dd/MM/yyyy.");
        }

        if (blogs != null && blogs.isEmpty()) {
            model.addAttribute("infoMessage", "Không có blog nào khớp theo tìm kiếm của bạn.");
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        return "displayAllBlog";
    }
}
