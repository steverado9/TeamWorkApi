package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifCommentResponse;
import org.springframework.http.ResponseEntity;

public interface GifCommentService {
    ResponseEntity<ApiResponse<DataGifCommentResponse>> postComment(Long gifId, CommentDto commentDto);
}
