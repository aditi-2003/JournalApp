package com.aditi.journal_app.controller;

import com.aditi.journal_app.model.User;
import com.aditi.journal_app.repository.UserRepository;
import com.aditi.journal_app.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

//Think of it as the entry point of your authentication system.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository; //talks to the database (save users, find by username).
    private final JwtUtil jwtUtil;//creates JWT tokens.
    private final PasswordEncoder passwordEncoder;//hashes passwords before saving, and checks passwords at login.

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lock down signup : only user role can signup not admin
        user.setRoles(Set.of("ROLE_USER", "ROLE_VIEWER"));

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
        }

        // Build UserDetails object for JwtUtil
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        // Generate JWT with roles inside claims
        String token = jwtUtil.generateToken(username, user.getRoles());

        // Return as JSON
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("roles", user.getRoles());

        return ResponseEntity.ok(response);
    }

}

