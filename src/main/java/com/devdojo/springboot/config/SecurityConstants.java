package com.devdojo.springboot.config;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    // Authorization Bearer qualquer coisa
    public static final String SECRET = "DevDojo";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final long EXPIRATION_TIME = 120000L;

    public static void main(String[] args) {
        System.out.println(TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES));
    }
}
