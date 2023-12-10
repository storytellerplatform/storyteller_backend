package com.example.storyteller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "emotion")
@Table(name = "emotion")
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

    @Column(
            name = "emotion",
            nullable = false
    )
    private Integer emotion;
}
