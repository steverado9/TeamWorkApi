package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.FeedItemDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<List<FeedItemDto>>> feed() {
        return feedService.viewAllArticlesAndGifs();
    }
}
