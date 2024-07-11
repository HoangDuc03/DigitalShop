package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.service.CaptchaService;
import com.example.ShopAcc.service.UserServiceQ;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Controller
@AllArgsConstructor
@SessionAttributes({"userdto", "sessionId", "infor", "captcha"})
public class LoginController {

    private final UserServiceQ userService;
    private final CaptchaService captchaService;

    @ModelAttribute("userdto")
    public UserDto userDto() {
        return new UserDto();
    }

    @ModelAttribute("captcha")
    public String captcha(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId != null) {
            return captchaService.generateCaptchaForId(sessionId);
        } else {
            return ""; // Handle the case where sessionId is null
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session, HttpServletRequest request) {
        String sessionId = UUID.randomUUID().toString();
        session = request.getSession(true);
        session.setAttribute("sessionId", sessionId);

        String captcha = captchaService.generateCaptchaForId(sessionId);
        model.addAttribute("captcha", captcha);

        if (!model.containsAttribute("userdto")) {
            model.addAttribute("userdto", new UserDto());
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userdto") UserDto userDto,
                        @RequestParam("inputCaptcha") String inputCaptcha,
                        @SessionAttribute("sessionId") String sessionId,
                        @SessionAttribute("captcha") String captcha,
                        Model model, HttpSession session) throws NoSuchAlgorithmException {

        boolean isCaptchaCorrect = captchaService.verifyCaptcha(sessionId, inputCaptcha);

        if (!isCaptchaCorrect) {
            String newCaptcha = captchaService.generateCaptchaForId(sessionId);
            model.addAttribute("captcha", newCaptcha);
            model.addAttribute("captchawrong", "Incorrect Captcha, please input again!!!");
            return "login";
        }

        boolean doesUserExist = userService.checkUserByEmail(userDto.getEmail());

        if (!doesUserExist) {
            model.addAttribute("emailwrong", "Email does not exist! Please try again!");
            return "login";
        }

        boolean isPasswordCorrect = userService.checkPasswordUser(userDto.getEmail(), userDto.getPassword());

        if (!isPasswordCorrect) {
            model.addAttribute("passwordwrong", "Password invalid! Please re-enter");
            return "login";
        }

        User user = userService.getUserByEmail(userDto.getEmail());
        model.addAttribute("userdto", userDto);
        session.setAttribute("infor", user);
        userDto.setUserDisplayName(user.getFullName());

        session.setAttribute("userId", user.getId()); // Store userId in session

        if (user.getRoleID().getRoleID() == 1) {
            return "redirect:/admin_home";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/generateCaptcha")
    @ResponseBody
    public String regenerateCaptcha(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        String newCaptcha = captchaService.generateCaptchaForId(sessionId);
        return newCaptcha;
    }
}
