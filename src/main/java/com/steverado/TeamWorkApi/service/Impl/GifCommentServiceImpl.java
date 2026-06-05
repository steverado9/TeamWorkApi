package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.entity.GifComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.repository.GifCommentRepository;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifCommentResponse;
import com.steverado.TeamWorkApi.service.GifCommentService;
import com.steverado.TeamWorkApi.service.GifService;
import com.steverado.TeamWorkApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GifCommentServiceImpl implements GifCommentService {

    @Autowired
    private GifCommentRepository gifCommentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GifService gifService;

    @Override
    public ResponseEntity<ApiResponse<DataGifCommentResponse>> postComment(Long gifId, CommentDto commentDto) {

        //get loggedin user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Gif> gif = gifService.getGifById(gifId);
        if (gif.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        GifComment comment = new GifComment();
        comment.setComment(commentDto.getComment());
        comment.setUser(currentUser.get());
        comment.setGif(gif.get());

        gifCommentRepository.saveComment(comment.getComment(), comment.getUser().getId(), comment.getGif().getId());

        Optional<GifComment> gifComment = gifCommentRepository.getGifCommentByGifId(gifId);

        if (gifComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataGifCommentResponse data = new DataGifCommentResponse();
        data.setMessage("comment successfully created");
        data.setCreatedOn(gifComment.get().getCreatedAt());
        data.setGifTitle(gif.get().getTitle());
        data.setComment(gifComment.get().getComment());

        ApiResponse<DataGifCommentResponse> response = new ApiResponse<>("“success”", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
