package com.example.hw14.web.auth;

import com.example.hw14.web.dto.LoginDto;

public interface TokenService {
    boolean checkAuthenticationData(LoginDto loginDto);

    void setAuthenticationData(Token token, String cardNumber, String pinCode);

    void logout(Token token);
}
