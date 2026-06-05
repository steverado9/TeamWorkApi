package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataFeedResponse;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.FeedService;
import com.steverado.TeamWorkApi.service.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private GifService gifService;

    @Override
    public ResponseEntity<ApiResponse<DataFeedResponse>> viewAllArticlesAndGifs() {
        List<Article> articles = articleService.getAllArticles();
        List<Gif> gifs = gifService.getAllGifs();
        return null;
    }
}
