package com.example.ShopAcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServiceI {

    @Value("${spring.mail.sender.username}")
    private String fromEmail;
    private final JavaMailSender senderMailSender;
    @Autowired

    public EmailService(@Qualifier("senderMailSender") JavaMailSender senderMailSender) {
        this.senderMailSender = senderMailSender;

    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        senderMailSender.send(message);
    }
}
