package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.FeedItemDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Feed Management", description = "APIs For viewing articles and gifs in the Feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @Operation(summary = "View feed", description = "View all the articles and gifs in the feed")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "View feed")
    })
    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<List<FeedItemDto>>> feed() {
        return feedService.viewAllArticlesAndGifs();
    }
}
