package com.example.storyteller.controller;

import com.example.storyteller.dto.AddNewArticleRequest;
import com.example.storyteller.dto.DeleteArticleRequest;
import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.User;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            return ResponseEntity.ok().body(userService.getUserData(currentUserEmail));
        }

        throw new CustomException("USER_NOT_FOUND", "使用者不存在");
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserData(@PathVariable Integer userId) {
        return ResponseEntity.ok().body(userService.getUserData(userId));
    }

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
    public ResponseEntity<List<Article>> addNewArticle(@RequestBody @NotNull AddNewArticleRequest request) {
        return ResponseEntity.ok(userService.addNewArticle(request.getContent(), request.getUserId(), request.getEmotionList()));
    }

    @DeleteMapping(path = "/article")
    public ResponseEntity<String> deleteArticle(@RequestBody DeleteArticleRequest request) {
        userService.deleteArticle(request.getArticleId(), request.getUserId());
        return ResponseEntity.ok("Delete article" + request.getArticleId() + "successfully");
    }
}