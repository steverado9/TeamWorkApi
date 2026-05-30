package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import com.steverado.TeamWorkApi.response.DeleteDataResponse;
import org.springframework.http.ResponseEntity;

public interface GifService {
    ResponseEntity<ApiResponse<DataGifResponse>> saveGif(GifDto gifDto);

    ResponseEntity<ApiResponse<DeleteDataResponse>> deleteGifById(Long id);
}
