package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import com.steverado.TeamWorkApi.response.DeleteDataResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface GifService {
    ResponseEntity<ApiResponse<DataGifResponse>> saveGif(GifDto gifDto);

    ResponseEntity<ApiResponse<DeleteDataResponse>> deleteGifById(Long id);

    Optional<Gif> getGifById(Long id);

    List<Gif> getAllGifs();
}
