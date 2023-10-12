package com.example.storyteller.controller;

import com.example.storyteller.dto.AuthenticationRequest;
import com.example.storyteller.dto.AuthenticationResponse;
import com.example.storyteller.dto.RegisterRequest;
import com.example.storyteller.service.AuthenticationService;
import com.example.storyteller.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response = authenticationService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
