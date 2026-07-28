package com.steverado.TeamWorkApi.repository;

import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GifRepository extends JpaRepository<Gif, Long> {

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO gifs (image_url, title, user_id, created_at)
            VALUES (:imageUrl, :title, :userId, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void saveGif(
            @Param("imageUrl") String imageUrl,
            @Param("title") String title,
            @Param("userId") Long userId);

    //The highest ID is usually the newest record.
    @Query(value = """
            SELECT * FROM gifs
            WHERE user_id = :userId
            AND id = LAST_INSERT_ID();
            """, nativeQuery = true)
    Optional<Gif> findGifByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM gifs WHERE id = :gifId", nativeQuery = true)
    void deleteGifById(@Param("gifId") Long gifId);

    @Query(value = "SELECT * FROM gifs WHERE id = :gifId", nativeQuery = true)
    Optional<Gif> findGifById(@Param("gifId") Long gifId);

    @Query(value = "SELECT * FROM gifs", nativeQuery = true)
    List<Gif> findAllGifs();
}
