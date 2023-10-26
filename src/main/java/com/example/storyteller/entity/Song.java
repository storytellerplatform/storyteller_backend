package com.example.storyteller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Song")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "song")
public class Song {

    @Id
    @SequenceGenerator(
            name = "song_sequence",
            sequenceName = "song_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "song_sequence"
    )
    private Integer songId;
}
