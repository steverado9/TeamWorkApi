package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    ResponseEntity<ArticleResponse<DataArticleResponse>> saveArticle(ArticleDto articleDto);

    Optional<Article> getArticleById(Long articleId);

    Optional<Article> findArticleByUserId(Long userId);

    ResponseEntity<ApiResponse<UpdateArticleDataResponse>> updateArticle(Long articleId, Article article);

    ResponseEntity<ApiResponse<DeleteDataResponse>> deleteArticle(Long articleId);

    List<Article> getAllArticles();
}
