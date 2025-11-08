package com.frontend.frontend;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final TokenStore tokenStore;

    public CustomAuthenticationProvider(TokenStore tokenStore) {
        this.tokenStore = Objects.requireNonNull(tokenStore, "tokenStore");
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final Object credentials = authentication.getCredentials();
        final String password = credentials == null ? "" : credentials.toString();

        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", name);
        requestBody.add("password", password);

        final RestTemplate restTemplate = new RestTemplate();
        final String token;

        try {
            final var responseEntity = restTemplate.postForEntity("http://localhost:8080/login", requestBody, String.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                throw new BadCredentialsException("Respuesta inesperada del servicio de autenticación");
            }
            token = responseEntity.getBody().replace("{\"token\":\"", "").replace("\"}", "");
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos", ex);
        } catch (Exception ex) {
            log.error("Error al autenticar contra el backend", ex);
            throw new BadCredentialsException("Error al autenticar: " + ex.getMessage(), ex);
        }

        tokenStore.setToken(token);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(name, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
