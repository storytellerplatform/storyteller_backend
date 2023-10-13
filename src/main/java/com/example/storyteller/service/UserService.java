package com.example.storyteller.service;

import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.Emotion;
import com.example.storyteller.entity.User;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public Boolean checkUsernameExists(String username) {
        return userRepository.existsByName(username);
    }

    public Boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<Article> getArticlesById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userId + " not found"));
        return user.getArticles();
    }

    public Article addNewArticle(String content, Integer userId, List<String> emotionList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userId + " not found"));

        Emotion emotion = Emotion.builder()
                .emotions(emotionList)
                .build();

        List<Emotion> newEmotionList = new ArrayList<>();

        newEmotionList.add(emotion);

        Article newArticle = Article.builder()
                .content(content)
                .emotions(newEmotionList)
                .build();

        List<Article> articleList = user.getArticles();
        if (articleList == null) {
            articleList = new ArrayList<>();
        }

        articleList.add(newArticle);
        user.setArticles(articleList);

        userRepository.save(user);
        return newArticle;
    }

    public void deleteArticle(Integer articleId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userId + " not found"));

        List<Article> articleList = user.getArticles();
    }

    public User getUserData(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "未找到" + userId + "使用者"));
    }

    public User getUserData(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

}
