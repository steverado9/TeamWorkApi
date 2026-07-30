package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.exceptions.ArticleNotFoundException;
import com.steverado.TeamWorkApi.exceptions.NotAdminException;
import com.steverado.TeamWorkApi.mappers.ArticleMapper;
import com.steverado.TeamWorkApi.mappers.CommentItemsMapper;
import com.steverado.TeamWorkApi.repository.ArticleCommentRepository;
import com.steverado.TeamWorkApi.repository.ArticleRepository;
import com.steverado.TeamWorkApi.response.*;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentItemsMapper commentItemsMapper;

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    public Optional<User> authenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userService.findUserByEmail(email);
    }

    @Override
    public ResponseEntity<ApiResponse> saveArticle(ArticleDto articleDto) {
        logger.info("Received request to create article with title: {}", articleDto.getTitle());

        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));
        logger.info("Authenticated user: {} (id={})", currentUser.getEmail(), currentUser.getId());

        Article article = articleMapper.toEntity(articleDto);
        article.setUser(currentUser);

        logger.debug("Saving article '{}' for user {}", article.getTitle(), currentUser.getId());
        articleRepository.saveArticle(article.getTitle(), article.getContent(), article.getUser().getId());

        Optional<Article> savedArticle = articleRepository.findArticleByUserId(currentUser.getId());
        logger.info("Article saved successfully with id {}", savedArticle.get().getId());

        DataArticleResponse data = new DataArticleResponse();
        data.setMessage("Article successfully posted");
        data.setArticleId(savedArticle.get().getId());
        data.setCreatedOn(savedArticle.get().getCreatedAt());
        data.setTitle(article.getTitle());

        ApiResponse<DataArticleResponse> response = new ApiResponse<>("success", data);

        logger.info("Returning CREATED response for article '{}'", article.getTitle());
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
        logger.info("Received request to update article with title: {} (and id={})", article.getTitle(), articleId);

        //get loggedin user
        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        //get existing article with id
        Article existingArticle = getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));

        //get user that created the article
        User articleUser = existingArticle.getUser();

        //if not admin
        if (currentUser.getRole() != Role.ADMIN && currentUser != articleUser) {
            throw new NotAdminException("FORBIDDEN!");
        }

        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        articleRepository.updateArticle(existingArticle.getTitle(),existingArticle.getContent(), articleUser.getId());

        UpdateArticleDataResponse data = new UpdateArticleDataResponse();
        data.setMessage("Article successfully updated");
        data.setTitle(existingArticle.getTitle());
        data.setArticle(existingArticle.getContent());

        ApiResponse<UpdateArticleDataResponse> response = new ApiResponse<UpdateArticleDataResponse>("Success", data);
        logger.info("Returning UPDATED response for article '{}'", existingArticle.getTitle());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteArticle(Long articleId) {
        logger.info("Received request to delete article with id: {}", articleId);

        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));
        logger.info("Authenticated user: {}", currentUser.getEmail());

        //get existing article with id
        Article existingArticle = getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));
        logger.info("Existing article: {}", existingArticle.getTitle());

        //get user that created the article
        User articleUser = existingArticle.getUser();

        if (currentUser.getRole() != Role.ADMIN && currentUser != articleUser) {
            throw new NotAdminException("FORBIDDEN!");
        }

        logger.debug("deleting article '{}' with id {}", existingArticle.getTitle(), existingArticle.getId());
        articleCommentRepository.deleteCommentsByArticleId(articleId);
        articleRepository.deleteArticleById(articleId);

        DeleteDataResponse data = new DeleteDataResponse();
        data.setMessage("Article successfully deleted");

        ApiResponse<DeleteDataResponse> response = new ApiResponse<>("Success", data);
        logger.info("Returning DELETED response for article '{}'", existingArticle.getTitle());

        return ResponseEntity.ok(response);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAllArticles();
    }


    @Override
    public ResponseEntity<ApiResponse> getArticleAndCommentById(Long articleId) {
        logger.info("Get article and comments using article id: {}", articleId);

        Article article = getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));
        logger.info("article title: {}", article.getTitle());

        List<ArticleComment> comments = articleCommentRepository.getAllCommentsByArticleId(articleId);

        List<CommentItemsDto> articleComments =
                comments.stream()
                        //map each article comment to the standard response comment(CommentItemDto) using method reference
                        .map(commentItemsMapper::articleComment)
                        .toList();


        DataViewArticleResponse<List<CommentItemsDto>> data = new DataViewArticleResponse<List<CommentItemsDto>>();
        data.setId(article.getId());
        data.setCreatedOn(article.getCreatedAt());
        data.setTitle(article.getTitle());
        data.setArticle(article.getContent());
        data.setComments(articleComments);

        ApiResponse<DataViewArticleResponse<List<CommentItemsDto>>> response = new ApiResponse<DataViewArticleResponse<List<CommentItemsDto>>>("success", data);
        logger.info("Returning Article and comments response for article title '{}'", article.getTitle());

        return ResponseEntity.ok(response);
    }
}
