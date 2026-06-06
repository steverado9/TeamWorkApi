package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.repository.ArticleCommentRepository;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataArticleCommentResponse;
import com.steverado.TeamWorkApi.service.ArticleCommentService;
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
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<ApiResponse<DataArticleCommentResponse>> saveComment(Long articleId, CommentDto commentDto) {

        //get loggedin user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Article> article = articleService.getArticleById(articleId);
        if (article.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ArticleComment comment = new ArticleComment();
        comment.setComment(commentDto.getComment());
        comment.setUser(currentUser.get());
        comment.setArticle(article.get());

        articleCommentRepository.saveArticleComment(comment.getComment(), comment.getArticle().getId(), comment.getUser().getId());

        Optional<ArticleComment> articleComment = articleCommentRepository.getArticleCommentByArticleId(article.get().getId());

        if (articleComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataArticleCommentResponse data = new DataArticleCommentResponse();
        data.setMessage("Comment successfully created");
        data.setCreatedOn(articleComment.get().getCreatedAt());
        data.setArticleTitle(article.get().getTitle());
        data.setArticle(article.get().getContent());
        data.setComment(articleComment.get().getComment());

        ApiResponse<DataArticleCommentResponse> response = new ApiResponse<DataArticleCommentResponse>("success", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public List<ArticleComment> getAllCommentsByArticleId(Long articleId) {
        return articleCommentRepository.getAllCommentsByArticleId(articleId);
    }
}
