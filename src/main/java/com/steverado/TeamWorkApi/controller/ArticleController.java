package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.response.*;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse<DataArticleResponse>> createArticle(@RequestBody ArticleDto articleDto) {

        return articleService.saveArticle(articleDto);
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<UpdateArticleDataResponse>> UpdateArticle(@PathVariable Long articleId, @RequestBody Article article) {

        return articleService.updateArticle(articleId, article);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<DeleteDataResponse>> deleteArticle(@PathVariable Long articleId) {

        return articleService.deleteArticle(articleId);
    }
}
