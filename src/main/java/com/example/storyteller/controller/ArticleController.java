package com.example.storyteller.controller;

import com.example.storyteller.entity.Article;
import com.example.storyteller.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping(path = "/emotion")
    public ResponseEntity<List<Article>> getArticlesByEmotion(
            @RequestParam @NotNull Integer userId,
            @RequestParam @NotNull Integer emotion
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticlesByEmotion(userId, emotion));
    }

    @GetMapping(path = "/name")
    public ResponseEntity<List<Article>> getArticlesByName(
            @RequestParam @NotNull Integer userId,
            @RequestParam @NotNull String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticlesByName(userId, search));
    }

    @GetMapping(path = "/sortedData")
    public ResponseEntity<List<Article>> getArticleSortByCreatedDate(
            @RequestParam @NotNull Integer userId,
            @RequestParam @NotNull ArticleService.SortDirection sort
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticleSortByCreatedDate(userId, sort));
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

    @PostMapping(path = "/collect/{articleId}")
    public ResponseEntity<Article> createAudio(@PathVariable Integer articleId, @RequestParam("audio") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.createAudio(articleId, file));
    }

    @GetMapping(path = "/newest/audioId/{articleId}")
    public ResponseEntity<Integer> getNewestAudioId(@PathVariable Integer articleId) {
        Integer newestAudioId = articleService.getNewestAudioId(articleId);
        return ResponseEntity.ok(newestAudioId);
    }

}
