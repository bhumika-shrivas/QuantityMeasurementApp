package com.app.quantitymeasurement.config;

import com.app.quantitymeasurement.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired private JwtAuthFilter jwtAuthFilter;
    @Autowired private OAuth2SuccessHandler oAuth2SuccessHandler;

    // ─── PUBLIC URLs (no token needed) ───
    private static final String[] PUBLIC_URLS = {
        "/auth/**",           // register, login
        "/oauth2/**",         // Google OAuth2 flow
        "/login/oauth2/**",   // Google callback
        "/h2-console/**",     // H2 database console
        "/swagger-ui/**",     // Swagger UI
        "/swagger-ui.html",
        "/api-docs/**",       // OpenAPI JSON
        "/actuator/health",   // health check
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(h -> h.frameOptions(
                    HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            // STATELESS — no sessions, JWT on every request
            .sessionManagement(s -> s
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers("/users/all").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // Google OAuth2
            .oauth2Login(oauth2 -> oauth2
                    .successHandler(oAuth2SuccessHandler))
            // JWT filter runs before every request
            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}