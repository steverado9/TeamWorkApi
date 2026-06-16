package com.steverado.TeamWorkApi.repository;

import com.steverado.TeamWorkApi.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO article_comments (comment, article_id, user_id, created_at)
            VALUES (:comment, :articleId, :userId, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void saveArticleComment(
            @Param("comment") String comment,
            @Param("articleId") Long ArticleId,
            @Param("userId") Long userId);

    @Query(value = """
            SELECT * FROM article_comments
            WHERE article_id = :articleId
            ORDER BY comment_id DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<ArticleComment> getArticleCommentByArticleId(@Param("articleId") Long articleId);

    @Query(value = """
            SELECT * FROM article_comments
            WHERE article_id = :articleId
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<ArticleComment> getAllCommentsByArticleId(@Param("articleId") Long articleId);

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM article_comments
            WHERE article_id = :articleId
            """, nativeQuery = true)
    void deleteCommentsByArticleId(@Param("articleId") Long gifId);
}
