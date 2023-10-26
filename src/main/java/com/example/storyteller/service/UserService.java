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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "使用者 %s 未找到";

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

        Article newArticle = Article.builder()
                .content(content)
                .emotions(Collections.singletonList(emotion))
                .build();

        user.getArticles().add(newArticle);
        userRepository.save(user);

        return user.getArticles().get(user.getArticles().size() - 1);
    }


    public void deleteArticle(Integer articleId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        List<Article> articleList = user.getArticles();
        articleList.remove(articleList.get(articleId));

        user.setArticles(articleList);
    }

    public User getUserData(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));
    }

    public User getUserData(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, email)));
    }

    public Integer enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
