package com.example.storyteller.service;

import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.User;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        return user.getArticles();
    }


    public List<Article> getArticlesSortedByDate(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        List<Article> articleList = user.getArticles();

        return articleList;
    }

    public Article addNewArticle(String name, String content, Integer userId, List<Integer> emotions) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        Article newArticle = Article.builder()
                .name(name)
                .content(content)
                .emotions(emotions)
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

    @Transactional
    public User getUserData(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));
    }

    public User getUserData(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, email)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, email)));
    }

    public User partiallyUpdateUser (Integer userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "使用者不存在"));

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }

        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getArticles() != null) {
            user.setArticles(updatedUser.getArticles());
        }

        return userRepository.save(user);
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

    @Transactional
    public User findByGoogleId(String googleId) {
        Optional<User> user = userRepository.findByGoogleId(googleId);
        return user.orElse(null);
    }

    public boolean existsByGoogleId(String googleId) { return userRepository.existsByGoogleId(googleId); }

    public void save(User user) {
        userRepository.save(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
