package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataArticleCommentResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleCommentService {
    ResponseEntity<ApiResponse> saveComment(Long articleId, CommentDto commentDto);

    List<ArticleComment> getAllCommentsByArticleId(Long articleId);
}
