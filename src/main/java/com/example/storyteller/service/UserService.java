package com.example.storyteller.service;

import com.example.storyteller.dto.ApiResponse;
import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.User;
import com.example.storyteller.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<Article> getArticlesByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return user.getArticles();
    }

    public List<Article> addNewArticle(Article article, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        List<Article> articleList = user.getArticles();
        if (articleList == null) {
            articleList = new ArrayList<>();
        }
        articleList.add(article);
        user.setArticles(articleList);

        userRepository.save(user);
        return articleList;
    }

    public void deleteArticle(Long articleId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        List<Article> articleList = user.getArticles();
    }
}
