package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataArticleCommentResponse;
import org.springframework.http.ResponseEntity;

public interface ArticleCommentService {
    ResponseEntity<ApiResponse<DataArticleCommentResponse>> saveComment(Long articleId, CommentDto commentDto);
}
