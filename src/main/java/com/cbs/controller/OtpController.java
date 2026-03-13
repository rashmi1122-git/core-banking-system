package com.cbs.controller;

import com.cbs.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/generate-otp")
    public String generateOtp(@RequestParam String username) {
        otpService.generateOtp(username);
        return "OTP sent to registered email";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String username,
                            @RequestParam String otp) {
        otpService.verifyOtp(username, otp);
        return "OTP verified successfully";
    }


}
