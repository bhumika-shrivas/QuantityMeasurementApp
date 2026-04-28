package com.app.quantitymeasurement.service.auth;

import com.app.quantitymeasurement.dto.*;
import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.model.Role.RoleName;
import com.app.quantitymeasurement.repository.*;
import com.app.quantitymeasurement.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
/**
 * AuthService provides core authentication and registration operations used
 * by the application.
 *
 * Primary responsibilities:
 * - register(RegisterRequest): validate input, ensure uniqueness of email
 *   and username, encode the password, assign a default role (ROLE_USER),
 *   persist the new User, and return an AuthResponse containing a JWT and
 *   user details.
 * - login(LoginRequest): authenticate credentials using Spring Security's
 *   AuthenticationManager, issue a JWT for the authenticated user, and
 *   return an AuthResponse containing token and user details including
 *   assigned roles.
 *
 * Notes:
 * - This service delegates role lookup to RoleRepository and persistence
 *   to UserRepository. Password encoding is handled by a PasswordEncoder.
 * - Errors are surfaced as RuntimeExceptions here for brevity; in a
 *   production system you should map errors to appropriate HTTP status
 *   codes and error payloads.
 */
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;

    // ─── REGISTER ───
    /**
     * Registers a new user.
     * Steps:
     * 1. Check email and username uniqueness
     * 2. Fetch the ROLE_USER role
     * 3. Build a User entity, encoding the password
     * 4. Persist the user and issue a JWT
     */
    public AuthResponse register(RegisterRequest request) {
        // Check duplicates
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already registered: " + request.getEmail());

        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already taken: " + request.getUsername());

        // Assign ROLE_USER by default
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Build and save user
        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider("LOCAL")
                .roles(Set.of(userRole))
                .enabled(true)
                .build();

        userRepository.save(user);

        // Generate token
        String token = jwtUtils.generateTokenFromEmail(user.getEmail());

        // Build a response DTO containing token and user profile
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(List.of("ROLE_USER"))
                .build();
    }

    // ─── LOGIN ───
    /**
     * Authenticates a user and returns an AuthResponse with JWT and user info.
     * Steps:
     * 1. Authenticate credentials using AuthenticationManager
     * 2. Generate JWT for the authenticated principal
     * 3. Load user details and map roles to strings for the response
     */
    public AuthResponse login(LoginRequest request) {
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        // Generate token
        String token = jwtUtils.generateToken(authentication);

        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map Role enum values to their string names for the response
        List<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .build();
    }
}