package com.example.storyteller.repository;

import com.example.storyteller.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query(value = "SELECT * FROM article WHERE user_id = :userId AND :emotion = ANY(emotions)", nativeQuery = true)
    List<Article> findByUserIdAndEmotion(Integer userId, Integer emotion);

    @Query(value = "SELECT * FROM article WHERE user_id = :userId ORDER BY created_date DESC", nativeQuery = true)
    List<Article> findAllByOrderByCreatedDateDesc(Integer userId);

    @Query(value = "SELECT * FROM article WHERE user_id = :userId ORDER BY created_date ASC", nativeQuery = true)
    List<Article> findAllByOrderByCreatedDateAsc(Integer userId);

    List<Article> findByNameContaining(String keyword);
}
