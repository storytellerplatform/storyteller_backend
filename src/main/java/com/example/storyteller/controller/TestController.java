package com.example.storyteller.controller;

import com.example.storyteller.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public ApiResponse<String> hello() {
        return new ApiResponse<>("Hello world!");
    }
}
