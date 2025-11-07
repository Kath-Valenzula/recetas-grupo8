package com.demo.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .cors(c -> c.configurationSource(req -> {
              CorsConfiguration cfg = new CorsConfiguration();
              cfg.setAllowedOrigins(List.of("http://localhost:8081"));
              cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
              cfg.setAllowedHeaders(List.of("Authorization","Content-Type"));
              cfg.setAllowCredentials(true);
              return cfg;
          }))
          .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // ajustar si usas Thymeleaf forms
          .headers(h -> h
              .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; img-src 'self' https:; script-src 'self'"))
              .contentTypeOptions(Customizer.withDefaults())
              .referrerPolicy(r -> r.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
              .frameOptions(frame -> frame.sameOrigin()) // solo si usas H2 console
          )
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/","/login","/css/**","/js/**","/images/**","/recetas/public/**","/actuator/health","/actuator/info").permitAll()
              .requestMatchers(HttpMethod.GET,"/recetas/**").authenticated()
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .anyRequest().authenticated()
          )
          .formLogin(form -> form.loginPage("/login").permitAll())
          .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());
        return http.build();
    }

    // Demo: usuarios en memoria para la entrega (si ya tienes UserDetailsService, elimina esto)
        @Bean
        @Primary
        UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
          User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build(),
          User.withUsername("chef").password(encoder.encode("chef123")).roles("CHEF").build(),
          User.withUsername("user").password(encoder.encode("user123")).roles("USER").build()
        );
    }

    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean DaoAuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder enc) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }
}