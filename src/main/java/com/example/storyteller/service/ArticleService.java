package com.example.storyteller.service;

import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.Audio;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.repository.ArticleRepository;
import com.example.storyteller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final static String USER_NOT_FOUND_MSG =
            "使用者 %s 未找到";

    public enum SortDirection {
        ASC,
        DESC
    }

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Article getArticleById(Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));
    }

    public List<Article> getArticlesByEmotion(Integer userId, Integer emotion) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        return articleRepository.findByUserIdAndEmotion(userId, emotion);
    }

    public List<Article> getArticlesByName(Integer userId, String search) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        return articleRepository.findByNameContaining(search);
    }


    public List<Article> getArticleSortByCreatedDate(Integer userId, @NotNull SortDirection sortDirection) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", String.format(USER_NOT_FOUND_MSG, userId)));

        if (sortDirection == SortDirection.ASC) {
            return articleRepository.findAllByOrderByCreatedDateAsc(userId);
        }

        if (sortDirection == SortDirection.DESC) {
            return articleRepository.findAllByOrderByCreatedDateDesc(userId);
        }

        return articleRepository.findAll();
    }

    @Transactional
    public Article updateArticle(Integer articleId, Article article) {
        articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        article.setArticleId(articleId);

        return articleRepository.save(article);
    }

    @Transactional
    public Article partiallyUpdateArticle(Integer articleId, Article updatedArticle) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        if (updatedArticle.getContent() != null) {
            article.setContent(updatedArticle.getContent());
        }

        if (updatedArticle.getPurpose() != null) {
            article.setPurpose(updatedArticle.getPurpose());
        }

        return articleRepository.save(article);
    }

    @Transactional
    public Article createNewEmotions(Integer articleId, List<Integer> emotions) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        article.setEmotions(emotions);

        return articleRepository.save(article);
    }

    @Transactional
    public Article createAudio(Integer articleId, MultipartFile file) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        try {
            Audio audio = Audio.builder()
                    .audioData(file.getBytes())
                    .build();

            article.getAudioList().add(audio);

            return articleRepository.save(article);
        } catch (IOException e) {
            throw new CustomException("FILE_READ_ERROR", String.format("無法讀取檔案 %s", e));
        }
    }

    public Integer getNewestAudioId(Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        if (article.getNewestAudioId() == null) {
            throw new CustomException("AUDIO_NOT_FOUND", "此文章未有音檔!");
        }

        return article.getNewestAudioId();
    }


}
