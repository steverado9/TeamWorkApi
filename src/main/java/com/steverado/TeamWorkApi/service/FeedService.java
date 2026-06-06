package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.FeedItemDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FeedService {
    ResponseEntity<ApiResponse<List<FeedItemDto>>> viewAllArticlesAndGifs();
}
