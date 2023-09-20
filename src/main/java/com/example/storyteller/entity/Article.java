package com.example.storyteller.entity;

import jakarta.persistence.*;

@Entity(name = "Article")
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
    private Long articleId;

    @Column(
            name = "content",
            columnDefinition = "TEXT"
    )
    private String content;
}
