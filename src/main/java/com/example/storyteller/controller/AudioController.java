package com.example.storyteller.controller;

import com.example.storyteller.service.AudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/audio")
@RequiredArgsConstructor
public class AudioController {
    private final AudioService audioService;

    @GetMapping
    public ResponseEntity<?> getAudios() {
        return ResponseEntity.status(HttpStatus.OK).body(audioService.getAllAudios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAudio(@PathVariable("id") Integer audioId) {
        byte[] file = audioService.getAudio(audioId).getAudioData();

        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Audio Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(file);
    }

    @PostMapping
    public ResponseEntity<String> uploadAudio(@RequestParam("audio") MultipartFile file) {
        try {
            audioService.uploadAudio(file);
            return ResponseEntity.status(HttpStatus.OK).body("Audio file uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload audio file");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAudio(@PathVariable("id") Integer audioId) {
        return ResponseEntity.status(HttpStatus.OK).body(audioService.deleteAudio(audioId));
    }
}
