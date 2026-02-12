package com.example.employee_management.controller;

import com.example.employee_management.dto.auth.AuthRequest;
import com.example.employee_management.dto.auth.AuthResponse;
import com.example.employee_management.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String token = jwtUtil.generateToken(request.username());

        return new AuthResponse(token);
    }

}




