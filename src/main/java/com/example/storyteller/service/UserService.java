package com.example.storyteller.service;

import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.User;
import com.example.storyteller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<Article> getArticlesByUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getArticles();
    }
}
