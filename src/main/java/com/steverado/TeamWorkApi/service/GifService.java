package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import com.steverado.TeamWorkApi.response.DataViewGifResponse;
import com.steverado.TeamWorkApi.response.DeleteDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface GifService {
    Optional<User> authenticateUser();

    ResponseEntity<ApiResponse> saveGif(GifDto gifDto, MultipartFile file);

    ResponseEntity<ApiResponse> deleteGifById(Long id);

    Optional<Gif> getGifById(Long id);

    List<Gif> getAllGifs();

    ResponseEntity<ApiResponse> getGifAndCommentByGifId(Long id);
}
