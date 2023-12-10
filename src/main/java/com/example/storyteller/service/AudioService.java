package com.example.storyteller.service;

import com.example.storyteller.entity.Audio;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.repository.AudioRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AudioService {
    private final static String AUDIO_NOT_FOUND_MSG =
            "音檔 %s 未找到";

    private final AudioRepository audioRepository;

    public List<Audio> getAllAudios() {
        return audioRepository.findAll();
    }

    public Audio getAudio(Integer audioId) {
        return audioRepository.findById(audioId)
                .orElseThrow(() -> new CustomException("AUDIO_NOT_FOUND", String.format(AUDIO_NOT_FOUND_MSG, audioId)));
    }

    public void uploadAudio(@NotNull MultipartFile file) throws IOException {
        Audio audio = Audio.builder()
                .audioData(file.getBytes())
                .build();

        audioRepository.save(audio);
    }

    public String deleteAudio(Integer audioId) {
        audioRepository.findById(audioId)
                .orElseThrow(() -> new CustomException("AUDIO_NOT_FOUND", String.format(AUDIO_NOT_FOUND_MSG, audioId)));

        audioRepository.deleteById(audioId);
        return "音檔成功刪除!";
    }
}
