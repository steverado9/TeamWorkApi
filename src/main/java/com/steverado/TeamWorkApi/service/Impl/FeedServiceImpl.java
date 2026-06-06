package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.FeedItemDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.FeedService;
import com.steverado.TeamWorkApi.service.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private GifService gifService;

    @Override
    public ResponseEntity<ApiResponse<List<FeedItemDto>>> viewAllArticlesAndGifs() {
        List<Article> articles = articleService.getAllArticles();
        List<Gif> gifs = gifService.getAllGifs();

        List<FeedItemDto> feed = new ArrayList<>();

        articles.stream().forEach(article -> {
            feed.add(new FeedItemDto(
                    article.getId(),
                    article.getTitle(),
                    article.getContent(),
                    article.getCreatedAt(),
                    article.getUser().getId()
            ));
        });

        gifs.forEach(gif -> {
            feed.add(new FeedItemDto(
                    gif.getId(),
                    gif.getTitle(),
                    gif.getImageUrl(),
                    gif.getCreatedAt(),
                    gif.getUser().getId()
            ));
        });

        System.out.println("feeds content -> :" + feed);

        feed.sort(Comparator.comparing(FeedItemDto::getCreatedOn).reversed());

        ApiResponse<List<FeedItemDto>> response = new ApiResponse<>("success", feed);

        return ResponseEntity.ok(response);
    }
}
