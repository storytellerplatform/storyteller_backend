package com.example.storyteller.controller;

import com.example.storyteller.dto.AddNewArticleRequest;
import com.example.storyteller.dto.ApiResponse;
import com.example.storyteller.entity.Article;
import com.example.storyteller.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/checkUsername")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        return ResponseEntity.ok().body(userService.checkUsernameExists(username));
    }

    @GetMapping(path = "/checkEmail")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.checkEmailExists(email));
    }

    @RequestMapping(path = "/article")
    public ResponseEntity<List<Article>> getArticleByEmail(@RequestBody String email) {
        return ResponseEntity.ok(userService.getArticlesByEmail(email));
    }

    @PostMapping(path = "/article")
    public ResponseEntity<List<Article>> addNewArticle(@RequestBody AddNewArticleRequest request) {
        return ResponseEntity.ok(userService.addNewArticle(request.getArticle(), request.getEmail()));
    }

    @DeleteMapping(path = "/article")
    public ResponseEntity<String> deleteArticle(@RequestBody String email, @RequestBody Long articleId) {
        userService.deleteArticle(articleId, email);
        return ResponseEntity.ok("Delete article" + articleId + "successfully");
    }
}
