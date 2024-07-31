package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.service.CaptchaService;
import com.example.ShopAcc.service.UserServiceQ;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@SessionAttributes({"userdto", "infor"})
public class LoginController {

    @Autowired
    private UserServiceQ userService;

    @Autowired
    private CaptchaService captchaService;

    HttpSession session;

    @ModelAttribute("userdto")
    public UserDto userDto() {
        return new UserDto();
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        // Invalidate current session to clear any old data
//        session.invalidate();
        session = request.getSession(true);
        session.setAttribute("userdto", new UserDto());

        // Generate new captcha
        int captchaID = captchaService.CreateCaptchaID();
        session.setAttribute("CaptchaID", captchaID); // Store in session
        String captcha = captchaService.CreateCaptchaForID(captchaID);
        session.setAttribute("captcha", captcha);
        model.addAttribute("captcha", captcha);

        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userdto") UserDto userDto,
                        Model model,
                        @RequestParam("inputCaptcha") String inputCaptcha,
                        HttpSession session) {

        int captchaID = (int) session.getAttribute("CaptchaID");
        String captcha = (String) session.getAttribute("captcha");

        if (!userService.checkUserbyEmail(userDto.getEmail())) {
            return "redirect:/login?emailwrong";
        }

        User user = userService.getUserbyEmail(userDto.getEmail());

        if (!userService.checkPasswordUser(userDto.getEmail(), userDto.getPassword())) {
            return "redirect:/login?passwordwrong";
        }

        boolean status = user.isStatus();
        if(status) {
            return "redirect:/login?Inactive";
        }

        if (captchaService.VerifyCaptcha(captcha, captchaID, inputCaptcha)) {
            model.addAttribute("userdto", userDto);

            session.setAttribute("infor", user);

            session.setAttribute("userId", user.getId()); // Store userId in session

            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin");

            if (redirectAfterLogin != null) {
                return "redirect:" + redirectAfterLogin;
            } else {
                return "redirect:/home";
            }
        }

        return "redirect:/login?captchawrong";
    }

    @GetMapping("/generateCaptcha")
    @ResponseBody
    public String regenerateCaptcha() {
        int captchaID = captchaService.CreateCaptchaID();
        session.setAttribute("CaptchaID", captchaID); // Store in session
        String captcha = captchaService.CreateCaptchaForID(captchaID);
        session.setAttribute("captcha", captcha);
        return captcha;
    }
}
