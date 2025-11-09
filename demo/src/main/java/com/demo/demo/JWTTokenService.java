package com.demo.demo;

import static com.demo.demo.Constants.BEARER_PREFIX;
import static com.demo.demo.Constants.SUPER_SECRET_KEY;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.demo.demo.security.JWTAuthenticationConfig;

import io.jsonwebtoken.Jwts;

// FIX: Provides JWT issuance while delegating signing-key creation to security helper.
@Configuration
public class JWTTokenService {

    public String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1440))
                .and()
                .signWith(JWTAuthenticationConfig.getSigningKey(SUPER_SECRET_KEY))
                .compact();

        return BEARER_PREFIX + token;
    }
}
