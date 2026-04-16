package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.model.Role;
import com.app.quantitymeasurement.model.Role.RoleName;
import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.repository.RoleRepository;
import com.app.quantitymeasurement.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public OAuth2SuccessHandler(JwtUtils jwtUtils,
                                UserRepository userRepository,
                                RoleRepository roleRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String rawName = oAuth2User.getAttribute("name");
        String rawGoogleId = oAuth2User.getAttribute("sub");

        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Google account email not found");
            return;
        }

        final String normalizedName =
                (rawName == null || rawName.isBlank()) ? email.split("@")[0] : rawName;

        final String normalizedGoogleId =
                (rawGoogleId == null || rawGoogleId.isBlank()) ? email : rawGoogleId;

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

            String username = email.split("@")[0];

            return userRepository.save(User.builder()
                    .email(email)
                    .username(username)
                    .fullName(normalizedName)
                    .provider("GOOGLE")
                    .providerId(normalizedGoogleId)
                    .roles(Set.of(userRole))
                    .enabled(true)
                    .build());
        });

        String token = jwtUtils.generateTokenFromEmail(user.getEmail());

        String roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(","));

        String redirectUrl = "http://localhost:4200/?"
                + "token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&type=Bearer"
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8)
                + "&username=" + URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8)
                + "&fullName=" + URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8)
                + "&roles=" + URLEncoder.encode(roles, StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        clearAuthenticationAttributes(request);
    }
}