package com.example.storyteller.entity;

import com.example.storyteller.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Attribute")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attribute")
public class Emotion {
    @Id
    @SequenceGenerator(
            name = "emotion_sequence",
            sequenceName = "emotion_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "emotion_sequence"
    )
    private Integer emotionId;

    @Column(name = "emotions")
    private List<String> emotions;

    private static final Set<String> ALLOWED_EMOTIONS = new HashSet<>(
            List.of("開心", "悲傷", "浪漫", "憤怒", "緊張")
    );

    public void setEmotions(List<String> emotions) {
        // 验证emotions列表中的字符串是否合法
        for (String emotion : emotions) {
            if (!ALLOWED_EMOTIONS.contains(emotion)) {
                throw new CustomException("INVALID_EMOTION", "Invalid emotion: " + emotion);
            }
        }
        this.emotions = emotions;
    }
}
