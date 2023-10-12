package com.example.storyteller.dto;

import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNewArticleRequest {
    private Integer userId;
    private String content;
    private List<String> emotionList;
}
