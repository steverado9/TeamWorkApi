package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.UpdateArticleDataResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ArticleService {
    Optional<Article> saveArticle(ArticleDto articleDto, User user);

    Optional<Article> findArticleByUserId(Long userId);

    Optional<Article> getArticleById(Long articleId);

    ResponseEntity<ApiResponse<UpdateArticleDataResponse>> updateArticle(Long articleId, Article article);
}
