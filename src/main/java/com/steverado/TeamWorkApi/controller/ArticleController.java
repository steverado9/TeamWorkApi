package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.ArticleResponse;
import com.steverado.TeamWorkApi.response.DataArticleResponse;
import com.steverado.TeamWorkApi.response.UpdateArticleDataResponse;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ArticleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse<DataArticleResponse>> createArticle(@RequestBody ArticleDto articleDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Article> article = articleService.saveArticle(articleDto, currentUser.get());

        if (article.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataArticleResponse data = new DataArticleResponse();
        data.setMessage("Article successfully posted");
        data.setArticleId(article.get().getId());
        data.setCreatedOn(article.get().getCreatedAt());
        data.setTitle(article.get().getTitle());

        ArticleResponse<DataArticleResponse> response = new ArticleResponse<>(article.get().getTitle(), article.get().getContent(), "success", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<UpdateArticleDataResponse>> UpdateArticle(@PathVariable Long articleId, @RequestBody Article article) {

        return articleService.updateArticle(articleId, article);
    }
}
