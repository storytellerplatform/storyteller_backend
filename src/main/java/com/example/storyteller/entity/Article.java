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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity(name = "Article")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "article")
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @SequenceGenerator(
            name = "article_sequence",
            sequenceName = "article_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "article_sequence"
    )
    @Column(name = "article_id")
    private Integer articleId;

    @Column(
            name = "content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(
            name = "purpose"
    )
    private String purpose;

    @Column(
            name = "name"
    )
    private String name;


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id"
    )
    @JsonIgnore
    private User user;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(
            name = "emotions"
    )
    private List<Integer> emotions;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "article_id",
            referencedColumnName = "article_id"
    )
    @JsonIgnore
    private List<Audio> audioList;

    public Integer getNewestAudioId() {
        if (audioList == null || audioList.isEmpty()) {
            return null;
        }

        Optional<Audio> newestAudio = audioList.stream()
                .filter(audio -> audio.getCreatedDate() != null)
                .max(Comparator.comparing(Audio::getCreatedDate));

        return newestAudio.map(Audio::getAudioId).orElse(null);
    }

    public List<Integer> getAllAudioIds() {
        if (audioList == null || audioList.isEmpty()) {
            return null;
        }

        return audioList.stream()
                .map(Audio::getAudioId)
                .collect(Collectors.toList());
    }

    public Integer userId() {
        return this.user.getUserId();
    }
}
