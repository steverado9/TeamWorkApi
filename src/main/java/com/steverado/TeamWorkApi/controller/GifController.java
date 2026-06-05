package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifCommentResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import com.steverado.TeamWorkApi.response.DeleteDataResponse;
import com.steverado.TeamWorkApi.service.GifCommentService;
import com.steverado.TeamWorkApi.service.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GifController {

    @Autowired
    private GifService gifService;

    @Autowired
    private GifCommentService gifCommentService;

    @PostMapping("/gifs")
    public ResponseEntity<ApiResponse<DataGifResponse>> postGif(@RequestBody GifDto gifDto) {

        return gifService.saveGif(gifDto);
    }

    @DeleteMapping("/gifs/{id}")
    public ResponseEntity<ApiResponse<DeleteDataResponse>> deleteGif(@PathVariable Long id) {

        return gifService.deleteGifById(id);
    }

    @PostMapping("/gifs/{gifId}/comment")
    public ResponseEntity<ApiResponse<DataGifCommentResponse>> postComment( @PathVariable Long gifId, @RequestBody CommentDto commentDto) {
        return gifCommentService.postComment(gifId, commentDto);
    }
}
