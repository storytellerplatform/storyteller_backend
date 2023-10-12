package com.example.storyteller.entity;

import com.example.storyteller.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Article")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "article")
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
    private Integer articleId;

    @Column(
            name = "content",
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(
            name = "purpose",
            insertable = false
    )
    private String purpose;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "article_id",
            referencedColumnName = "articleId"
    )
    private List<Emotion> emotions;

}
