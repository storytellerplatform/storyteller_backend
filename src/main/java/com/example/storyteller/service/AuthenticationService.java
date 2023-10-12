package com.example.storyteller.service;

import com.example.storyteller.dto.AuthenticationRequest;
import com.example.storyteller.dto.AuthenticationResponse;
import com.example.storyteller.dto.RegisterRequest;
import com.example.storyteller.entity.*;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public boolean isValidPassword(String password) {
        Pattern englishPattern = Pattern.compile("[a-zA-Z]");
        Pattern digitPattern = Pattern.compile("\\d");

        boolean hasEnglish = englishPattern.matcher(password).find();
        boolean hasDigits = digitPattern.matcher(password).find();
        boolean hasValidLength = password.length() > 6;

        return hasEnglish && hasDigits && hasValidLength;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("ACCOUNT_EXISTS", "帳號已存在");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("EMAIL_EXISTS", "信箱已存在");
        }

        if (!isValidPassword((request.getPassword()))) {
            throw new CustomException("INVALID_PASSWORD", "密碼錯誤");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .expiresIn(1)
                .token(jwtToken)
                .build();
    }
}