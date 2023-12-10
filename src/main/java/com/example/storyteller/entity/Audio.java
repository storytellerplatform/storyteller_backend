package com.example.storyteller.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Audio")
@Table(name = "audio")
@EntityListeners(AuditingEntityListener.class)
public class Audio {

    @Id
    @SequenceGenerator(
            name = "audio_sequence",
            sequenceName = "audio_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "audio_sequence"
    )
    private Integer audioId;

    @Lob
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(
            name = "audio_data",
            nullable = false
    )
    private byte[] audioData;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
