package com.example.storyteller.controller;

import com.example.storyteller.dto.AddNewArticleRequest;
import com.example.storyteller.dto.DeleteArticleRequest;
import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.User;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserData(currentUserEmail));
        }

        throw new CustomException("USER_NOT_FOUND", "使用者不存在");
    }

    @GetMapping("/info")
    public Map<String, Object> user(@NotNull OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
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

    @GetMapping(path = "/article/{userId}")
    public ResponseEntity<List<Article>> getArticleById(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getArticlesById(userId));
    }

    @GetMapping(path = "/sortedByDate/{userId}")
    public ResponseEntity<List<Article>> getArticlesSortedByDate(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getArticlesSortedByDate(userId));
    }

    @PostMapping(path = "/article")
    public ResponseEntity<Article> addNewArticle(@RequestBody @NotNull AddNewArticleRequest request) {
        return ResponseEntity.ok(userService.addNewArticle(request.getName(), request.getContent(), request.getUserId(), request.getEmotions()));
    }

    @DeleteMapping(path = "/article")
    public ResponseEntity<String> deleteArticle(@RequestBody DeleteArticleRequest request) {
        userService.deleteArticle(request.getArticleId(), request.getUserId());
        return ResponseEntity.ok("Delete article" + request.getArticleId() + "successfully");
    }
}
