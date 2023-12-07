package com.example.storyteller.service;

import com.example.storyteller.entity.Audio;
import com.example.storyteller.repository.AudioRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioService {
    private final AudioRepository audioRepository;

    public List<Audio> getAllAudios() {
        return audioRepository.findAll();
    }

    public Audio getAudio(Integer audioId) {
        return audioRepository.findById(audioId).orElse(null);
    }

    public void uploadAudio(@NotNull MultipartFile file) throws IOException {
        Audio audio = Audio.builder()
                .audioData(file.getBytes())
                .build();

        audioRepository.save(audio);
    }
}
