package com.steverado.TeamWorkApi.repository;

import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.entity.GifComment;
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
public interface GifCommentRepository extends JpaRepository<GifComment, Long> {
    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO gif_comments (comment, gif_id, user_id, created_at)
            VALUES (:comment, :gifId, :userId, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void saveComment(
            @Param("comment") String comment,
            @Param("userId") Long userId,
            @Param("gifId") Long gifId);

    @Query(value = """
            SELECT * FROM gif_comments
            WHERE gif_id = :gifId
            ORDER BY comment_id DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<GifComment> getGifCommentByGifId(@Param("gifId") Long gifId);

    @Query(value = """
            SELECT * FROM gif_comments
            WHERE gif_id = :gifId
            ORDER BY comment_id DESC
            """, nativeQuery = true)
    List<GifComment> getAllCommentsByGifId(@Param("gifId") Long gifId);

}
