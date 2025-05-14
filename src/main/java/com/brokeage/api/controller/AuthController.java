package com.brokeage.api.controller;

import com.brokeage.api.dto.BRApiResponse;
import com.brokeage.api.dto.LoginRequest;
import com.brokeage.api.dto.LoginResponse;
import com.brokeage.api.model.Role;
import com.brokeage.api.model.User;
import com.brokeage.api.repository.UserRepository;
import com.brokeage.api.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<BRApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtUtil.generateToken(auth.getName());
        return ResponseEntity.ok(BRApiResponse.success("Login successful", new LoginResponse(token)));
    }

    @PostMapping("/register")
    public ResponseEntity<BRApiResponse<String>> register(@Valid @RequestBody LoginRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(BRApiResponse.error("Username already exists"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        return ResponseEntity.ok(BRApiResponse.success("User registered successfully"));
    }

}
