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

    @Override
    public ResponseEntity<ApiResponse> saveComment(Long articleId, CommentDto commentDto) {

        User currentUser = articleService.authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        Article article = articleService.getArticleById(articleId).orElseThrow(() -> new ArticleNotFoundException("article not found"));

        //used commentMapper to insert the data from commentDto to the article comment
        ArticleComment comment = commentMapper.toArticleCommentEntity(commentDto);
        //the set the user and article manually
        comment.setUser(currentUser);
        comment.setArticle(article);

        articleCommentRepository.saveArticleComment(comment.getComment(), comment.getArticle().getId(), comment.getUser().getId());

        Optional<ArticleComment> articleComment = articleCommentRepository.getArticleCommentByArticleId(article.getId());

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
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public List<ArticleComment> getAllCommentsByArticleId(Long articleId) {
        return articleCommentRepository.getAllCommentsByArticleId(articleId);
    }
}
