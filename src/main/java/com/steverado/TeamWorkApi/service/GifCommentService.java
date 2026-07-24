package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifCommentResponse;
import com.steverado.TeamWorkApi.response.DataViewGifResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GifCommentService {
    ResponseEntity<ApiResponse> postComment(Long gifId, CommentDto commentDto);

}
