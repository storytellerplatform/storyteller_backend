package com.example.storyteller.config;

import com.example.storyteller.service.ConfirmationTokenService;
import com.example.storyteller.service.JwtService;
import com.example.storyteller.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final JwtService jwtService;
    private final ConfirmationTokenService confirmationTokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityConstants.PUBLIC_URLS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/oauth2/authorization/google")
//                        .successHandler((request, response, authentication) -> {
//                            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
//                            DefaultOAuth2User user = (DefaultOAuth2User) authToken.getPrincipal();
//                            Map<String, Object> attributes = user.getAttributes();
//                            System.out.println(attributes);
//
//                            String googleId = attributes.get("sub").toString();
//                            String name = attributes.get("name").toString();
//                            String email = attributes.get("email").toString();
//                            Boolean emailVerified = (Boolean) attributes.get("email_verified");
//
//                            User newUser = userService.findByGoogleId(googleId);
//                            boolean isExists = newUser != null;
//
//                            if (!isExists) {
//                                newUser = User.builder()
//                                    .googleId(googleId)
//                                    .name(name)
//                                    .email(email)
//                                    .isLocked(!emailVerified)
//                                    .enabled(emailVerified)
//                                    .role(Role.USER)
//                                    .build();
//
//                                userService.save(newUser);
//                            }
//
//                            // 更新 google 的狀態 ...
//                            // newUser.setName(name);
//
//                            String jwtToken = jwtService.generateToken(newUser);
//
//                            ConfirmationToken confirmationToken = ConfirmationToken
//                                    .builder()
//                                    .token(jwtToken)
//                                    .createdAt(LocalDateTime.now())
//                                    .expiresAt(LocalDateTime.now().plusMinutes(15))
//                                    .user(newUser)
//                                    .build();
//
//                            confirmationTokenService.saveConfirmationToken(confirmationToken);
//
//                            response.addHeader("Authorization", "Bearer " + jwtToken);
//                            response.sendRedirect("http://localhost:3000?token=" + jwtToken);
//                        })
//                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://aazz282828-alps:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
