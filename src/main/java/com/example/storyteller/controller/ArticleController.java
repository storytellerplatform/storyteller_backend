package com.example.storyteller.controller;

import com.example.storyteller.entity.Article;
import com.example.storyteller.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(path = "/{articleId}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer articleId) {
        return ResponseEntity.ok(articleService.getArticleById(articleId));
    }

    @PutMapping(path = "/{articleId}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer articleId, @RequestBody Article updatedArticle) {
        return ResponseEntity.ok(articleService.updateArticle(articleId, updatedArticle));
    }

    @PatchMapping(path = "/{articleId}")
    public ResponseEntity<Article> partiallyUpdateArticle(@PathVariable Integer articleId, @RequestBody Article updatedArticle) {
        return ResponseEntity.ok(articleService.partiallyUpdateArticle(articleId, updatedArticle));
    }

    @PostMapping(path = "/emotion/{articleId}")
    public ResponseEntity<Article> createNewEmotions (
            @PathVariable Integer articleId,
            @RequestBody List<Integer> emotions
    ) {
        return ResponseEntity.ok(articleService.createNewEmotions(articleId, emotions));
    }
}
