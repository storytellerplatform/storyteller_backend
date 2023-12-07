package com.example.storyteller.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Audio")
@Table(name = "audio")
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
    @Basic(fetch = FetchType.LAZY)
    @Column(
            name = "audio_data",
//            columnDefinition = "BYTEA",
            nullable = false
    )
    private byte[] audioData;
}
