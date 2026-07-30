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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GifCommentServiceImpl.class);

    @Override
    public ResponseEntity<ApiResponse> postComment(Long gifId, CommentDto commentDto) {
        logger.info("Received request to save comment in gif with gif id: {}", gifId);

        //get loggedin user
        User currentUser = gifService.authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));
        logger.info("Authenticated user: {} (id={})", currentUser.getEmail(), currentUser.getId());

        Gif gif = gifService.getGifById(gifId).orElseThrow(() -> new GifNotFoundException("Gif not found"));

        //used commentMapper to insert the data from commentDto to the gif comment
        GifComment comment = commentMapper.toGifCommentEntity(commentDto);

        //the set the user and gif manually
        comment.setUser(currentUser);
        comment.setGif(gif);

        logger.debug("Saving comment for article '{}' comment {}", gif.getTitle(), comment.getComment());
        gifCommentRepository.saveComment(comment.getComment(), comment.getUser().getId(), comment.getGif().getId());

        Optional<GifComment> gifComment = gifCommentRepository.getGifCommentByGifId(gifId);
        logger.info("Comment saved successfully with id {}", gifComment.get().getComment_id());

        if (gifComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataGifCommentResponse data = new DataGifCommentResponse();
        data.setMessage("comment successfully created");
        data.setCreatedOn(gifComment.get().getCreatedAt());
        data.setGifTitle(gif.getTitle());
        data.setComment(gifComment.get().getComment());

        ApiResponse<DataGifCommentResponse> response = new ApiResponse<>("“success”", data);
        logger.info("Returning Saved comment response for article '{}'", gif.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
