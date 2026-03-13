package com.cbs.security;

import com.cbs.entity.UserCredentials;
import com.cbs.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserCredentialsRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void validateLogin(String username, String password) {

        UserCredentials user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        // check if account locked
        if(!user.isAccountNonLocked()){
            throw new RuntimeException("Account locked due to multiple failed attempts");
        }

        // check password
        if(!passwordEncoder.matches(password, user.getPassword())){

            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if(user.getFailedAttempts() >= 5){
                user.setAccountNonLocked(false);
                //user.setLockTime(LocalDateTime.now());
            }

            repository.save(user);

            throw new RuntimeException("Invalid credentials");
        }


        if(!user.isAccountNonLocked()){

            if(user.getLockTime().plusMinutes(30).isBefore(LocalDateTime.now())){

                user.setAccountNonLocked(true);
                user.setFailedAttempts(0);
                repository.save(user);

            } else {
                throw new RuntimeException("Account locked. Try after 30 minutes");
            }
        }
        // reset attempts after successful login
        user.setFailedAttempts(0);
        repository.save(user);
    }
}
