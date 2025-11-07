package com.demo.demo;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.demo.demo.Constants.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;


@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

        private Claims setSigningKey(HttpServletRequest request) {
            String jwtToken = request.
                    getHeader(HEADER_AUTHORIZATION ).
                    replace(BEARER_PREFIX, "");

                    return Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey(SUPER_SECRET_KEY))
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

        }

        private void setAuthentication(Claims claims) {

            List<String> authorities = (List<String>) claims.get("authorities");

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                    authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        private boolean isJWTValid(HttpServletRequest request, HttpServletResponse res) {
            String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION);
            if (authenticationHeader == null || !authenticationHeader.startsWith(BEARER_PREFIX))
                return false;
            return true;
        }

        @Override
        protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {
            try {
                if (isJWTValid(request, response)) {
                    Claims claims = setSigningKey(request);
                    if (claims.get("authorities") != null) {
                        setAuthentication(claims);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                return;
            }
        }

    }