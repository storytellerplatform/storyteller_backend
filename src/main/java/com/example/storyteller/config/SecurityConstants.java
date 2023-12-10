package com.example.storyteller.config;

public class SecurityConstants {
    public static final String[] PUBLIC_URLS = {
            "/webjars/**",
            "/api/v1/auth/**",
            "/api/v1/test/**",
            "/api/v1/user/checkUsername",
            "/api/v1/user/checkEmail"
    };

}
