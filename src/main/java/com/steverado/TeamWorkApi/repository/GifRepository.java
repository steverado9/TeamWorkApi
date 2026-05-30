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
            @Param("userid") Long userid);

    @Query(value = "SELECT * FROM gifs WHERE user_id = userId", nativeQuery = true)
    Optional<Gif> findGifByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE * FROM gifs WHERE id = :gifId", nativeQuery = true)
    void deleteGifById(@Param("gifId") Long gifId);
}
