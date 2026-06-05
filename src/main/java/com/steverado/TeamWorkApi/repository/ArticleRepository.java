package com.steverado.TeamWorkApi.repository;

import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO articles (title, content, user_id, created_at)
            VALUES (:title, :content, :userId, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void saveArticle(
            @Param("title") String title,
            @Param("content") String content,
            @Param("userId") Long userId
    );

    //The highest ID is usually the newest record.
    @Query(value = """
            SELECT * FROM articles
            WHERE user_id = :userId
            ORDER BY id DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Article> findArticleByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM articles WHERE id = :articleId", nativeQuery = true)
    Optional<Article> findByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE articles
            SET title = :title, content = :content, created_at = CURRENT_TIMESTAMP
            WHERE id = :id
            """ , nativeQuery = true)
    void updateArticle(
            @Param("title") String title,
            @Param("content") String content,
            @Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM articles WHERE id = :articleId", nativeQuery = true)
    void deleteArticleById(@Param("articleId") Long articleId);

    @Query(value = "SELECT * FROM articles", nativeQuery = true)
    List<Article> findAllArticles();
}
