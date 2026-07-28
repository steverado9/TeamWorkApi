package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.entity.GifComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.exceptions.GifNotFoundException;
import com.steverado.TeamWorkApi.mappers.CommentMapper;
import com.steverado.TeamWorkApi.repository.GifCommentRepository;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifCommentResponse;
import com.steverado.TeamWorkApi.response.DataViewGifResponse;
import com.steverado.TeamWorkApi.service.GifCommentService;
import com.steverado.TeamWorkApi.service.GifService;
import com.steverado.TeamWorkApi.service.UserService;
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
public class GifCommentServiceImpl implements GifCommentService {

    @Autowired
    private GifCommentRepository gifCommentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GifService gifService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ResponseEntity<ApiResponse> postComment(Long gifId, CommentDto commentDto) {

        //get loggedin user
        User currentUser = gifService.authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        Gif gif = gifService.getGifById(gifId).orElseThrow(() -> new GifNotFoundException("Gif not found"));

        //used commentMapper to insert the data from commentDto to the gif comment
        GifComment comment = commentMapper.toGifCommentEntity(commentDto);

        //the set the user and gif manually
        comment.setUser(currentUser);
        comment.setGif(gif);

        gifCommentRepository.saveComment(comment.getComment(), comment.getUser().getId(), comment.getGif().getId());

        Optional<GifComment> gifComment = gifCommentRepository.getGifCommentByGifId(gifId);

        if (gifComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataGifCommentResponse data = new DataGifCommentResponse();
        data.setMessage("comment successfully created");
        data.setCreatedOn(gifComment.get().getCreatedAt());
        data.setGifTitle(gif.getTitle());
        data.setComment(gifComment.get().getComment());

        ApiResponse<DataGifCommentResponse> response = new ApiResponse<>("“success”", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
