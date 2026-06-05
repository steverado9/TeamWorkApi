package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.repository.ArticleRepository;
import com.steverado.TeamWorkApi.response.*;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public ResponseEntity<ArticleResponse<DataArticleResponse>> saveArticle(ArticleDto articleDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setUser(currentUser.get());
        articleRepository.saveArticle(article.getTitle(), article.getContent(), article.getUser().getId());

        Optional<Article> savedArticle = articleRepository.findArticleByUserId(currentUser.get().getId());

        DataArticleResponse data = new DataArticleResponse();
        data.setMessage("Article successfully posted");
        if (savedArticle.isPresent()) {
            data.setArticleId(savedArticle.get().getId());
            data.setCreatedOn(savedArticle.get().getCreatedAt());
        }
        data.setTitle(article.getTitle());

        ArticleResponse<DataArticleResponse> response = new ArticleResponse<>(article.getTitle(), article.getContent(), "success", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public Optional<Article> getArticleById(Long articleId) {
        return articleRepository.findByArticleId(articleId);
    }

    @Override
    public Optional<Article> findArticleByUserId(Long userId) {
        return articleRepository.findArticleByUserId(userId);
    }

    @Override
    public ResponseEntity<ApiResponse<UpdateArticleDataResponse>> updateArticle(Long articleId, Article article) {
        //get loggedin user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //get existing article with id
        Optional<Article> existingArticle = getArticleById(articleId);
        if (existingArticle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //get user that created the article
        User articleUser = existingArticle.get().getUser();

        if (currentUser.get().getRole() != Role.ADMIN && currentUser.get() != articleUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingArticle.get().setTitle(article.getTitle());
        existingArticle.get().setContent(article.getContent());
        articleRepository.updateArticle(existingArticle.get().getTitle(),existingArticle.get().getContent(), existingArticle.get().getUser().getId());

        UpdateArticleDataResponse data = new UpdateArticleDataResponse();
        data.setMessage("Article successfully updated");
        data.setTitle(existingArticle.get().getTitle());
        data.setArticle(existingArticle.get().getContent());

        ApiResponse<UpdateArticleDataResponse> response = new ApiResponse<UpdateArticleDataResponse>("Success", data);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse<DeleteDataResponse>> deleteArticle(Long articleId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //get existing article with id
        Optional<Article> existingArticle = getArticleById(articleId);
        if (existingArticle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //get user that created the article
        User articleUser = existingArticle.get().getUser();

        if (currentUser.get().getRole() != Role.ADMIN && currentUser.get() != articleUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        articleRepository.deleteArticleById(articleId);
        DeleteDataResponse data = new DeleteDataResponse();
        data.setMessage("Article successfully deleted");

        ApiResponse<DeleteDataResponse> response = new ApiResponse<>("Success", data);

        return ResponseEntity.ok(response);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAllArticles();
    }
}
