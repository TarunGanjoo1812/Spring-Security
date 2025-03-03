package com.security.feature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token){
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Please verify your Email (Spring Security)");
        message.setText("Hi, Click the link to verify your email: " + verificationLink);
        mailSender.send(message);
    }
}
