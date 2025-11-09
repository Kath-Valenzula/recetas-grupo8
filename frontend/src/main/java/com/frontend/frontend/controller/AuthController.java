package com.frontend.frontend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final String backendUrl = "http://localhost:8080/register";

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("email", email);
            requestBody.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            String methodStr = HttpMethod.POST.name();
            HttpMethod method = safeMethod(methodStr);
            // FIX: Ensure HttpMethod resolution is null-safe to satisfy static analysis.
            restTemplate.exchange(backendUrl, method, entity, String.class);

            model.addAttribute("message", "Usuario registrado correctamente. Ahora puedes iniciar sesion.");
        } catch (Exception ex) {
            log.error("Error al registrar usuario", ex);
            model.addAttribute("message", "Error al registrar usuario: " + ex.getMessage());
        }

        return "register";
    }

    private static @NonNull HttpMethod safeMethod(@NonNull String methodStr) {
        // FIX: Current Spring version lacks HttpMethod.resolve; emulate it via valueOf with null guard.
        HttpMethod resolved;
        try {
            resolved = HttpMethod.valueOf(methodStr);
        } catch (IllegalArgumentException ex) {
            resolved = null;
        }
        return Objects.requireNonNull(resolved, "HttpMethod invalido: " + methodStr);
    }
}

