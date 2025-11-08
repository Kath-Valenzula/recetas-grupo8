package com.demo.demo;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.demo.demo.Constants.*;

import javax.crypto.SecretKey;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

        private Claims setSigningKey(HttpServletRequest request) {
            String jwtToken = Objects.requireNonNull(request.getHeader(HEADER_AUTHORIZATION))
                    .replace(BEARER_PREFIX, "");

            return Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey(SUPER_SECRET_KEY))
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

        }

        private void setAuthentication(Claims claims) {
            List<?> authorities = claims.get("authorities", List.class);
            if (authorities == null) {
                SecurityContextHolder.clearContext();
                return;
            }

            List<GrantedAuthority> grantedAuthorities = authorities.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, grantedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        private boolean isJWTValid(HttpServletRequest request) {
            String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION);
            return authenticationHeader != null && authenticationHeader.startsWith(BEARER_PREFIX);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            try {
                if (isJWTValid(request)) {
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
            }
        }
}
