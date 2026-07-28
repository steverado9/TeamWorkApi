package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.Gif;
import com.steverado.TeamWorkApi.entity.GifComment;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.exceptions.GifNotFoundException;
import com.steverado.TeamWorkApi.exceptions.NotAdminException;
import com.steverado.TeamWorkApi.mappers.CommentItemsMapper;
import com.steverado.TeamWorkApi.repository.GifCommentRepository;
import com.steverado.TeamWorkApi.repository.GifRepository;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataGifResponse;
import com.steverado.TeamWorkApi.response.DataViewGifResponse;
import com.steverado.TeamWorkApi.response.DeleteDataResponse;
import com.steverado.TeamWorkApi.service.CloudinaryService;
import com.steverado.TeamWorkApi.service.GifService;
import com.steverado.TeamWorkApi.service.UserService;
import com.steverado.TeamWorkApi.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CommentItemsMapper commentItemsMapper;

    public Optional<User> authenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userService.findUserByEmail(email);
    }

    @Override
    public ResponseEntity<ApiResponse> saveGif(GifDto gifDto, MultipartFile file) {

        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);

        final String image_url = cloudinaryService.uploadFile(file);

        Gif gif = new Gif();
        gif.setTitle(gifDto.getTitle());
        gif.setImageUrl(image_url);
        gif.setUser(currentUser);

        gifRepository.saveGif(gif.getImageUrl(), gif.getTitle(), currentUser.getId());

        Gif savedGif = gifRepository.findGifByUserId(currentUser.getId()).orElseThrow(() -> new GifNotFoundException("Gif not found"));

        DataGifResponse data = new DataGifResponse();

        data.setGifId(savedGif.getId());
        data.setMessage("Gif Image Successfully Posted");
        data.setCreatedOn(savedGif.getCreatedAt());
        data.setTitle(gif.getTitle());
        data.setImageUrl(gif.getImageUrl());

        ApiResponse<DataGifResponse> response = new ApiResponse<DataGifResponse>("Success", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteGifById(Long id) {

        User currentUser = authenticateUser().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        //get existing gif with id
        Gif existingGif = getGifById(id).orElseThrow(() -> new GifNotFoundException("Gif not found"));

        //get user that created the gif
        User gifUser = existingGif.getUser();

        if (currentUser.getRole() != Role.ADMIN && currentUser != gifUser) {
            throw new NotAdminException("FORBIDDEN!");
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
    public ResponseEntity<ApiResponse> getGifAndCommentByGifId(Long id) {

        Gif gif = getGifById(id).orElseThrow(() -> new GifNotFoundException("Gif not found"));;

        List<GifComment> comments = gifCommentRepository.getAllCommentsByGifId(id);

        List<CommentItemsDto> GifComments = comments.stream()
                //maps each comment to the standard response gif comment(CommentItemsDto) using method reference.
                .map(commentItemsMapper::gifComment)
                .toList();

        DataViewGifResponse<List<CommentItemsDto>> data = new DataViewGifResponse<>();
        data.setId(gif.getId());
        data.setCreatedOn(gif.getCreatedAt());
        data.setTitle(gif.getTitle());
        data.setUrl(gif.getImageUrl());
        data.setComments(GifComments);

        ApiResponse<DataViewGifResponse<List<CommentItemsDto>>> response = new ApiResponse<>("success", data);

        return ResponseEntity.ok(response);
    }
}
