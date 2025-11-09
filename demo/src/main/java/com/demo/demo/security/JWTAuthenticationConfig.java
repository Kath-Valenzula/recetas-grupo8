package com.demo.demo.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

/**
 * FIX: Provides signing key generator so filters/services can avoid duplicating crypto logic.
 */
public final class JWTAuthenticationConfig {

    private JWTAuthenticationConfig() {
    }

    public static SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
