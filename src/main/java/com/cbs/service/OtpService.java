package com.cbs.service;

import com.cbs.entity.OtpVerification;
import com.cbs.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository repository;

    @Autowired
    private EmailService emailService;


    public String generateOtp(String username){

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        OtpVerification verification = new OtpVerification();
        verification.setUsername(username);
        verification.setOtp(otp);
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        repository.save(verification);

        emailService.sendOtp(username, otp);
        return otp;
    }


    public boolean verifyOtp(String username, String requestOtp){

        OtpVerification savedOtp = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if(savedOtp.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP Expired");
        }

        if(!savedOtp.getOtp().equals(requestOtp)){
            throw new RuntimeException("Invalid OTP");
        }

        repository.delete(savedOtp);

        return true;
    }


}