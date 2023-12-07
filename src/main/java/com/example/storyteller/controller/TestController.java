package com.example.storyteller.controller;

import com.example.storyteller.dto.ApiResponse;
import com.example.storyteller.service.AudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {
    private final AudioService audioService;

    @GetMapping("/test")
    public ApiResponse<String> hello() {
        return new ApiResponse<>("Hello world!");
    }

    @PostMapping("/test")
    public ResponseEntity<Integer> test(@RequestParam("a") Integer a) {
        return ResponseEntity.ok(a);
    }

    @GetMapping("/secure")
    public ApiResponse<String> secure() {
        return new ApiResponse<>("Secure!");
    }

}
