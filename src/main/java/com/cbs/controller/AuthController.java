package com.cbs.controller;

import com.cbs.dto.RefreshTokenRequestDTO;
import com.cbs.entity.RefreshToken;
import com.cbs.entity.Role;
import com.cbs.entity.UserCredentials;
import com.cbs.exception.DublicateCustomerException;
import com.cbs.repository.RefreshTokenRepository;
import com.cbs.repository.UserCredentialsRepository;
import com.cbs.security.AuthRequestDTO;
import com.cbs.security.AuthResponseDTO;
import com.cbs.security.AuthService;
import com.cbs.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserCredentialsRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String register(@RequestBody UserCredentials user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.BANK_EMPLOYEE);
        repository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        String accessToken = jwtService.generateToken(request.getUsername()); //JWT token generate here
        String refreshToken = UUID.randomUUID().toString();

        // fetch user
        UserCredentials user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new DublicateCustomerException("Username not found"));

        // save refresh token in DB
        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(token);
        return new AuthResponseDTO(accessToken);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refreshToken(@RequestBody String refreshToken){

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(token.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Refresh token expired");
        }
        String newAccessToken = jwtService.generateToken(token.getUser().getUsername());
        return new AuthResponseDTO(newAccessToken);
    }

}