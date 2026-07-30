package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.exceptions.ArticleNotFoundException;
import com.steverado.TeamWorkApi.mappers.CommentMapper;
import com.steverado.TeamWorkApi.repository.ArticleCommentRepository;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataArticleCommentResponse;
import com.steverado.TeamWorkApi.service.ArticleCommentService;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentMapper commentMapper;

    private static final Logger logger = LoggerFactory.getLogger(ArticleCommentServiceImpl.class);

    @Override
    public ResponseEntity<ApiResponse> saveComment(Long articleId, CommentDto commentDto) {
        logger.info("Received request to save comment in article with article id: {}", articleId);

        User currentUser = articleService.authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));
        logger.info("Authenticated user: {} (id={})", currentUser.getEmail(), currentUser.getId());


        Article article = articleService.getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));

        //used commentMapper to insert the data from commentDto to the article comment
        ArticleComment comment = commentMapper.toArticleCommentEntity(commentDto);
        //the set the user and article manually
        comment.setUser(currentUser);
        comment.setArticle(article);

        logger.debug("Saving comment for article '{}' comment {}", article.getTitle(), comment.getComment());
        articleCommentRepository.saveArticleComment(comment.getComment(), comment.getArticle().getId(), comment.getUser().getId());

        Optional<ArticleComment> articleComment = articleCommentRepository.getArticleCommentByArticleId(article.getId());
        logger.info("Comment saved successfully with id {}", articleComment.get().getComment_id());


        if (articleComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataArticleCommentResponse data = new DataArticleCommentResponse();
        data.setMessage("Comment successfully created");
        data.setCreatedOn(articleComment.get().getCreatedAt());
        data.setArticleTitle(article.getTitle());
        data.setArticle(article.getContent());
        data.setComment(articleComment.get().getComment());

        ApiResponse<DataArticleCommentResponse> response = new ApiResponse<DataArticleCommentResponse>("success", data);
        logger.info("Returning Saved comment response for article '{}'", article.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public List<ArticleComment> getAllCommentsByArticleId(Long articleId) {
        return articleCommentRepository.getAllCommentsByArticleId(articleId);
    }
}
