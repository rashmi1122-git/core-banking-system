package com.cbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Banking OTP Verification");
        message.setText("Your OTP for transaction verification is: " + otp);

        mailSender.send(message);
    }
}
