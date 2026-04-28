package com.cg.auth.service;

import com.cg.auth.dto.LoginRequest;
import com.cg.auth.dto.RegisterRequest;
import com.cg.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        // Here you would typically save the user in DB
        return jwtUtil.generateToken(request.getEmail());
    }

    public String login(LoginRequest request) {
        // Here you would typically verify user credentials against a DB
        return jwtUtil.generateToken(request.getEmail());
    }
}
