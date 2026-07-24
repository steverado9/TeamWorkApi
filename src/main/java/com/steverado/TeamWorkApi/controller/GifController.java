package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.dtos.GifDto;
import com.steverado.TeamWorkApi.response.*;
import com.steverado.TeamWorkApi.service.GifCommentService;
import com.steverado.TeamWorkApi.service.GifService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@SecurityRequirement(name = "bearerAuth")
public class GifController {

    @Autowired
    private GifService gifService;

    @Autowired
    private GifCommentService gifCommentService;

    @Operation(summary = "Post a gif image", description = "Takes a multipart file and processes it")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "gif posted successfully",
                    content = @Content(schema = @Schema(implementation = GifDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping(value = "/gifs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> postGif(
            @RequestBody GifDto gifDto,
            @RequestPart("file") MultipartFile file
    ) {

        return gifService.saveGif(gifDto, file);
    }

    @Operation(summary = "Delete a gif", description = "Delete a gif from the feed using the ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Gif deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Gif not found",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/gifs/{id}")
    public ResponseEntity<ApiResponse> deleteGif(@PathVariable Long id) {

        return gifService.deleteGifById(id);
    }

    @Operation(summary = "Post a comment", description = "Add a comment to the gif using the gifId")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "comment posted successfully",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping("/gifs/{gifId}/comment")
    public ResponseEntity<ApiResponse> postComment( @PathVariable Long gifId, @Valid @RequestBody CommentDto commentDto) {
        return gifCommentService.postComment(gifId, commentDto);
    }

    @Operation(summary = "Get gif by ID", description = "Retrieve gif's details using its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Gif found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Gif not found",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("gifs/{gifId}")
    public  ResponseEntity<ApiResponse> viewGif(@PathVariable Long gifId) {
        return gifService.getGifAndCommentByGifId(gifId);
    }
}
