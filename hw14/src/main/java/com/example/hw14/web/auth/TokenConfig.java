package com.example.hw14.web.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class TokenConfig {

    @Bean
    @SessionScope
    public Token sessionScopedToken() {
        return new Token();
    }
}
