package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.FeedItemDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.mappers.FeedMapper;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.FeedService;
import com.steverado.TeamWorkApi.service.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private GifService gifService;

    @Autowired
    private FeedMapper feedMapper;

    @Override
    public ResponseEntity<ApiResponse> viewAllArticlesAndGifs(int page, int size) {
        //get all the articles
        List<Article> articles = articleService.getAllArticles();
        //get all the gifs
        List<Gif> gifs = gifService.getAllGifs();

        //created an empty list
        List<FeedItemDto> feed = new ArrayList<>();

        //used feedmapper to map the content of each article to the standard response feed article(FeedItemDto), then add it to the feed list
        articles.stream().forEach(article -> {
            feed.add(feedMapper.feedArticle(article));
        });

        gifs.forEach(gif -> {
            feed.add(feedMapper.feedGif(gif));
        });

//        new FeedItemDto(
//                article.getId(),
//                article.getTitle(),
//                article.getContent(),
//                article.getCreatedAt(),
//                article.getUser().getId()
//        )
//
//        new FeedItemDto(
//                gif.getId(),
//                gif.getTitle(),
//                gif.getImageUrl(),
//                gif.getCreatedAt(),
//                gif.getUser().getId()
//        )



        feed.sort(Comparator.comparing(FeedItemDto::getCreatedOn).reversed()); //sort the feed by created at and reverse it.

        int start = page * size; //Calculate the start of the page
        int end = Math.min(start + size, feed.size());  //Calculate the end of the page

        if(start >= feed.size()) { //This is used to check if the page exists
            return ResponseEntity.ok(new ApiResponse<>("success", Collections.emptyList()));
        }

        List<FeedItemDto> paginatedFeed = feed.subList(start, end);

        return ResponseEntity.ok( new ApiResponse<>("success", paginatedFeed));
    }
}
