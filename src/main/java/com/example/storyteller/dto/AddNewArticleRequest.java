package com.example.storyteller.dto;

import com.example.storyteller.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNewArticleRequest {
    private String email;
    private Article article;
}
