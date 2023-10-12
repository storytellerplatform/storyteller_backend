package com.example.storyteller.service;

import com.example.storyteller.entity.Article;
import com.example.storyteller.entity.Emotion;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article updateArticle(Integer articleId, Article article) {
        articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        article.setArticleId(articleId);

        return articleRepository.save(article);
    }

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

    public Article createNewEmotions(Integer articleId, Emotion emotion) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException("ARTICLE_NOT_FOUND", "文章不存在"));

        List<Emotion> emotionList = article.getEmotions();
        if (emotionList == null) {
            emotionList = new ArrayList<>();
        }
        emotionList.add(emotion);
        article.setEmotions(emotionList);

        return articleRepository.save(article);
    }
}