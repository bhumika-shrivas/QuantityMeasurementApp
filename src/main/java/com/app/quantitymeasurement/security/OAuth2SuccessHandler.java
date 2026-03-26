package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.model.Role;
import com.app.quantitymeasurement.model.Role.RoleName;
import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.repository.RoleRepository;
import com.app.quantitymeasurement.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email    = oAuth2User.getAttribute("email");
        String name     = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        // Find existing user or create new one
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            return userRepository.save(User.builder()
                    .email(email)
                    .username(email.split("@")[0])
                    .fullName(name)
                    .provider("GOOGLE")
                    .providerId(googleId)
                    .roles(Set.of(userRole))
                    .enabled(true)
                    .build());
        });

        // Generate JWT and return it in the response body
        String token = jwtUtils.generateTokenFromEmail(user.getEmail());

        // Write token directly to response so user can copy it
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"token\":\"" + token + "\"," +
            "\"type\":\"Bearer\"," +
            "\"email\":\"" + user.getEmail() + "\"," +
            "\"message\":\"Copy this token and use it in Swagger as: Bearer <token>\"}"
        );
    }
}