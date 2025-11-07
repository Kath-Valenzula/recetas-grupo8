package com.demo.demo.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.models.User;
import com.demo.demo.repository.UserRepository;

@RestController
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest){
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","El usuario ya existe"));
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Usuario registrado exitosamente"));
    }

}
