package com.demo.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    @Bean
    @Primary
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }


import com.demo.demo.JWTAuthorizationFilter;

/**
 * Configuración de seguridad para la API de recetas
 * 
 * Endpoints públicos:
 * - GET /                    (página principal)
 * - GET /login              (formulario de login)
 * - GET /css/**             (recursos estáticos)
 * - GET /js/**              (recursos estáticos)
 * - GET /images/**          (recursos estáticos)
 * - GET /recetas/public/**  (recetas públicas)
 * - GET /actuator/health    (health check)
 * - GET /actuator/info      (info)
 * 
 * Endpoints protegidos:
 * - GET /recetas/**         (requiere autenticación)
 * - POST/PUT/DELETE /**     (requiere autenticación)
 * - GET/POST /admin/**      (requiere rol ADMIN)
 * 
 * Configuración de seguridad:
 * - CORS: habilitado para frontend (http://localhost:8081)
 * - CSRF: habilitado (required para forms)
 * - Headers: CSP, Referrer, Frame options
 * - Sesión: maxSessions=1, session fixation protection
 */

@Configuration
public class SecurityConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type"
        ));
        configuration.setExposedHeaders(List.of(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(c -> c.configurationSource(corsConfigurationSource()))
           .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
           .headers(h -> h
               .contentSecurityPolicy(csp -> csp
                   .policyDirectives("default-src 'self'; "
                       + "img-src 'self' https: data:; "
                       + "script-src 'self'; "
                       + "style-src 'self' 'unsafe-inline';"))
               .xssProtection(Customizer.withDefaults())
               .contentTypeOptions(Customizer.withDefaults())
               .referrerPolicy(r -> r.policy(
                   org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
               .frameOptions(frame -> frame.deny()))
           .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**",
                   "/recetas/public/**", "/actuator/health", "/actuator/info").permitAll()
               .requestMatchers(HttpMethod.GET, "/recetas/**").authenticated()
               .requestMatchers("/admin/**").hasRole("ADMIN")
               .anyRequest().authenticated())
           .formLogin(form -> form
               .loginPage("/login")
               .permitAll())
           .logout(l -> l
               .logoutUrl("/logout")
               .logoutSuccessUrl("/")
               .invalidateHttpSession(true)
               .deleteCookies("JSESSIONID")
               .permitAll())
           .sessionManagement(session -> session
               .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
               .maximumSessions(1)
               .maxSessionsPreventsLogin(true)
               .sessionRegistry(sessionRegistry())
               .and()
               .sessionFixation(fix -> fix.migrateSession()));
        
        return http.build();
    }

    // Demo: usuarios en memoria para la entrega (si ya tienes UserDetailsService, elimina esto)
        @Bean
        @Primary
        UserDetailsService userDetailsService(PasswordEncoder encoder) {
                var manager = new InMemoryUserDetailsManager();
                manager.createUser(User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build());
                manager.createUser(User.withUsername("chef").password(encoder.encode("chef123")).roles("CHEF").build()); 
                manager.createUser(User.withUsername("user").password(encoder.encode("user123")).roles("USER").build());
                return manager;
    }

    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean 
    @Primary
    DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService(passwordEncoder()));
        return provider;
    }
}