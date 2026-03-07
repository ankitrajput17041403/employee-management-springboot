package com.example.employee_management.controller;

import com.example.employee_management.dto.LoginRequestDto;
import com.example.employee_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto request) {

        //in this line spring will check authenticat the user which are trying to login..
        //Internally AuthenticatinMnagaer ->Userdetailservice->check password and send to the AuthenticationManager
        //Object will have usernformation kind user name,role, and user Authenticated kind.....
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        //Inside current logied in user details...
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(userDetails);
    }
}