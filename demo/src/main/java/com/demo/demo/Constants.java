package com.demo.demo;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class Constants {

    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String ISSUER_INFO = "DemoApp";
    public static final String SUPER_SECRET_KEY = "b8d5f49a3e2c9d6f1a0e7b5c2d3a9e8f1234567890abcdef";
    public static final long TOKEN_EXPIRATION_TIME = 86400000;

    public static Key getSigningKeyB64(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
