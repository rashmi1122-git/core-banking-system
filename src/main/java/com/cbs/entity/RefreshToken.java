package com.cbs.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table
public class RefreshToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String token;

        private LocalDateTime expiryDate;

        @OneToOne
        private UserCredentials user;


        public RefreshToken() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public LocalDateTime getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
        }

        public UserCredentials getUser() {
            return user;
        }

        public void setUser(UserCredentials user) {
            this.user = user;
        }
    }

