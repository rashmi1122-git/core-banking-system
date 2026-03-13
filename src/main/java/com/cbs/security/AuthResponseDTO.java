package com.cbs.security;

public class AuthResponseDTO {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthResponseDTO(String token) {
        this.token = token;
    }

}
