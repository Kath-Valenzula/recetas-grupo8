package com.demo.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityEventLogger.class);

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        logger.info("Login exitoso: usuario={}", auth.getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        logger.warn("Login fallido: usuario={}, error={}", 
            username, event.getException().getMessage());
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        if (auth != null) {
            logger.info("Logout exitoso: usuario={}", auth.getName());
        }
    }

}