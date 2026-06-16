package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.entity.GifComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.repository.GifCommentRepository;
import com.steverado.TeamWorkApi.repository.GifRepository;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import com.steverado.TeamWorkApi.response.DataViewGifResponse;
import com.steverado.TeamWorkApi.response.DeleteDataResponse;
import com.steverado.TeamWorkApi.service.GifService;
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
public class GifServiceImpl implements GifService {

    @Autowired
    private GifRepository gifRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GifCommentRepository gifCommentRepository;

    @Override
    public ResponseEntity<ApiResponse<DataGifResponse>> saveGif(GifDto gifDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Gif gif = new Gif();
        gif.setTitle(gifDto.getTitle());
        gif.setImageUrl(gifDto.getImageUrl());
        gif.setUser(currentUser.get());

        gifRepository.saveGif(gif.getImageUrl(), gif.getTitle(), currentUser.get().getId());

        Optional<Gif> savedGif = gifRepository.findGifByUserId(currentUser.get().getId());

        DataGifResponse data = new DataGifResponse();
        if (savedGif.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        data.setGifId(savedGif.get().getId());
        data.setMessage("Gif Image Successfully Posted");
        data.setCreatedOn(savedGif.get().getCreatedAt());
        data.setTitle(gif.getTitle());
        data.setImageUrl(gif.getImageUrl());

        ApiResponse<DataGifResponse> response = new ApiResponse<DataGifResponse>("Success", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse<DeleteDataResponse>> deleteGifById(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> currentUser = userService.findUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //get existing gif with id
        Optional<Gif> existingGif = getGifById(id);
        if (existingGif.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //get user that created the gif
        User gifUser = existingGif.get().getUser();

        if (currentUser.get().getRole() != Role.ADMIN && currentUser.get() != gifUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        gifCommentRepository.deleteCommentsWithGifId(id);
        gifRepository.deleteGifById(id);

        DeleteDataResponse data = new DeleteDataResponse();
        data.setMessage("gif post successfully deleted");

        ApiResponse<DeleteDataResponse> response = new ApiResponse<>("Success", data);

        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<Gif> getGifById(Long id) {
        return gifRepository.findGifById(id);
    }

    @Override
    public List<Gif> getAllGifs() {
        return gifRepository.findAllGifs();
    }

    @Override
    public ResponseEntity<ApiResponse<DataViewGifResponse<List<CommentItemsDto>>>> getGifAndCommentByGifId(Long id) {

        Optional<Gif> gif = getGifById(id);

        if (gif.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<GifComment> comments = gifCommentRepository.getAllCommentsByGifId(id);

        List<CommentItemsDto> GifComments = comments.stream()
                .map(comment -> new CommentItemsDto(
                        comment.getComment_id(),
                        comment.getComment(),
                        comment.getUser().getId()
                ))
                .toList();

        DataViewGifResponse<List<CommentItemsDto>> data = new DataViewGifResponse<>();
        data.setId(gif.get().getId());
        data.setCreatedOn(gif.get().getCreatedAt());
        data.setTitle(gif.get().getTitle());
        data.setUrl(gif.get().getImageUrl());
        data.setComments(GifComments);

        ApiResponse<DataViewGifResponse<List<CommentItemsDto>>> response = new ApiResponse<>("success", data);

        return ResponseEntity.ok(response);
    }
}
