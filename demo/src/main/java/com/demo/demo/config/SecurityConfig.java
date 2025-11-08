package com.demo.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.demo.demo.JWTAuthorizationFilter;

@Configuration
public class SecurityConfig {

    private static final String FRONTEND_ORIGIN = "http://localhost:8081";

    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(FRONTEND_ORIGIN));
        configuration.setAllowedMethods(List.of(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ));
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

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider)
            throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; "
                        + "img-src 'self' https: data:; "
                        + "script-src 'self'; "
                        + "style-src 'self' 'unsafe-inline';"))
                .xssProtection(Customizer.withDefaults())
                .contentTypeOptions(Customizer.withDefaults())
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
                .frameOptions(frame -> frame.deny()))
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/",
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/recetas/public/**",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/recetas/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").permitAll())
            .logout(logout -> logout
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
                .sessionFixation(fixation -> fixation.migrateSession()))
            .authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    @Primary
    UserDetailsService userDetailsService(PasswordEncoder encoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build());
        manager.createUser(User.withUsername("chef").password(encoder.encode("chef123")).roles("CHEF").build());
        manager.createUser(User.withUsername("user").password(encoder.encode("user123")).roles("USER").build());
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
