package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.exceptions.ArticleNotFoundException;
import com.steverado.TeamWorkApi.repository.ArticleCommentRepository;
import com.steverado.TeamWorkApi.repository.ArticleRepository;
import com.steverado.TeamWorkApi.response.*;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    public Optional<User> authenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userService.findUserByEmail(email);
    }

    @Override
    public ResponseEntity<ApiResponse> saveArticle(ArticleDto articleDto) {

        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));


        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setUser(currentUser);
        articleRepository.saveArticle(article.getTitle(), article.getContent(), article.getUser().getId());

        Optional<Article> savedArticle = articleRepository.findArticleByUserId(currentUser.getId());

        DataArticleResponse data = new DataArticleResponse();
        data.setMessage("Article successfully posted");
        if (savedArticle.isPresent()) {
            data.setArticleId(savedArticle.get().getId());
            data.setCreatedOn(savedArticle.get().getCreatedAt());
        }
        data.setTitle(article.getTitle());

        ApiResponse<DataArticleResponse> response = new ApiResponse<>("success", data);

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
    public ResponseEntity<ApiResponse> updateArticle(Long articleId, Article article) {

        //get loggedin user
        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        //get existing article with id
        Article existingArticle = getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));


        //get user that created the article
        User articleUser = existingArticle.getUser();

        if (currentUser.getRole() != Role.ADMIN && currentUser != articleUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        articleRepository.updateArticle(existingArticle.getTitle(),existingArticle.getContent(), existingArticle.getUser().getId());

        UpdateArticleDataResponse data = new UpdateArticleDataResponse();
        data.setMessage("Article successfully updated");
        data.setTitle(existingArticle.getTitle());
        data.setArticle(existingArticle.getContent());

        ApiResponse<UpdateArticleDataResponse> response = new ApiResponse<UpdateArticleDataResponse>("Success", data);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteArticle(Long articleId) {

        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        //get existing article with id
        Article existingArticle = getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));


        //get user that created the article
        User articleUser = existingArticle.getUser();

        if (currentUser.getRole() != Role.ADMIN && currentUser != articleUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        articleCommentRepository.deleteCommentsByArticleId(articleId);
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


    @Override
    public ResponseEntity<ApiResponse> getArticleAndCommentById(Long articleId) {

        Article article = getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));

        List<ArticleComment> comments = articleCommentRepository.getAllCommentsByArticleId(articleId);

        List<CommentItemsDto> articleComments =
                comments.stream()
                        .map(comment -> new CommentItemsDto(
                                comment.getComment_id(),
                                comment.getComment(),
                                comment.getUser().getId()
                        ))
                        .toList();


        DataViewArticleResponse<List<CommentItemsDto>> data = new DataViewArticleResponse<List<CommentItemsDto>>();
        data.setId(article.getId());
        data.setCreatedOn(article.getCreatedAt());
        data.setTitle(article.getTitle());
        data.setArticle(article.getContent());
        data.setComments(articleComments);

        ApiResponse<DataViewArticleResponse<List<CommentItemsDto>>> response = new ApiResponse<DataViewArticleResponse<List<CommentItemsDto>>>("success", data);
        return ResponseEntity.ok(response);
    }
}
