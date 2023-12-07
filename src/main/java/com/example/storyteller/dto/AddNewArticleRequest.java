package com.example.storyteller.dto;

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
    private String name;
    private String content;
    private List<Integer> emotions;
}
