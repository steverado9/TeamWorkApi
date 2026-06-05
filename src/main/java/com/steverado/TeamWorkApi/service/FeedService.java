package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataFeedResponse;
import org.springframework.http.ResponseEntity;

public interface FeedService {
    ResponseEntity<ApiResponse<DataFeedResponse>> viewAllArticlesAndGifs();
}
