package com.example.ShopAcc.service;

import com.example.ShopAcc.dto.ResetPasswordDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Role;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.model.otp;
import com.example.ShopAcc.repository.RoleRepository;
import com.example.ShopAcc.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService extends hash implements UserServiceI {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final EmailService emailService;

    @Override
    public ResponseEntity<ResponseObject> createUser(User user, HttpSession session) {
        if (user.getUserName() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed", "Please enter username and password", null));
        } else {
            Role role = roleRepository.findById(2).orElse(null);
            if (role == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("Failed", "Role not found", null));
            }
            User newUser = User.builder()
                    .fullName(user.getFullName())
                    .userName(user.getUserName())
                    .password(hashcode(user.getPassword()))
                    .phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail())
                    .createdAt(String.valueOf(new Date()))
                    .roleID(role)
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseObject("Successfully", "User registered", newUser));
        }
    }

    @Override
    public ResponseEntity<ResetPasswordDto> sendOtp(String email, HttpSession session) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResetPasswordDto("Failed", "Email not found", null));
        }

        String otp = generateOtp();
        otp inforesetpass = new otp(user,otp,3);
        session.setAttribute("otpresetpass",inforesetpass);

        emailService.sendOtpEmail(user.getEmail(), otp);
        return ResponseEntity.ok(new ResetPasswordDto("Success", "OTP has been sent to your email", user));
    }


    @Override
    public ResponseEntity<ResetPasswordDto> verifyOtp(String otp, HttpSession session) {
        otp inforesetpass = (otp) session.getAttribute("otpresetpass");
        if (inforesetpass == null || !otp.equals(inforesetpass.getOtp())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResetPasswordDto("Failed", "Invalid OTP. Please try again.", null));
        }
        session.setAttribute("resetPasswordUserId", inforesetpass.getUser().getId());
        session.removeAttribute("otpresetpass");
        return ResponseEntity.ok(new ResetPasswordDto("Success", "OTP verified", null));
    }
    public boolean checkEmailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }
    @Override
    public ResponseEntity<ResetPasswordDto> resetPassword(String newPassword, String reNewPassword, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("resetPasswordUserId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResetPasswordDto("Failed", "Invalid session", null));
        }

        if (!newPassword.equals(reNewPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResetPasswordDto("Failed", "Passwords do not match", null));
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResetPasswordDto("Failed", "User not found", null));
        }

        user.setPassword(hashcode(newPassword));
        userRepository.save(user);
        session.removeAttribute("resetPasswordUserId");
        return ResponseEntity.ok(new ResetPasswordDto("Success", "Password has been reset successfully", null));
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public UserDto getCurrentUserDto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            UserDto userDto = new UserDto();
            userDto.setEmail(userDetails.getUsername()); // Giả sử email làm username
            // Set các thông tin khác của user vào userDto nếu cần
            return userDto;
        }
        return null; // Trả về null nếu không có người dùng nào đang đăng nhập
    }
    public Page<User> getUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.searchUsersExcludingRole(1, keyword, pageable);
        }else return userRepository.findByRoleID_RoleIDIsNot(1,pageable);
    }

}
