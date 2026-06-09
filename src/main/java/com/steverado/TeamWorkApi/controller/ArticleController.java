package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.dtos.ArticleDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.response.*;
import com.steverado.TeamWorkApi.service.ArticleCommentService;
import com.steverado.TeamWorkApi.service.ArticleService;
import com.steverado.TeamWorkApi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@Tag(name = "Article Management", description = "APIS for managing Articles")
public class ArticleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCommentService articleCommentService;

    @Operation(summary = "Post an article", description = "Add a new article to the feed")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "article posted successfully",
                content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                content = @Content(schema = @Schema()))
    })
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse<DataArticleResponse>> createArticle(@RequestBody ArticleDto articleDto) {

        return articleService.saveArticle(articleDto);
    }

    @Operation(summary = "Update an article", description = "Update an existing article details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Article updated successfully",
                content = @Content(schema = @Schema(implementation = Article.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Article not found",
                content = @Content(schema = @Schema()))
    })
    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<UpdateArticleDataResponse>> UpdateArticle(@PathVariable Long articleId, @RequestBody Article article) {

        return articleService.updateArticle(articleId, article);
    }

    @Operation(summary = "Delete an article", description = "Delete an article from the feed using the ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Article deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<DeleteDataResponse>> deleteArticle(@PathVariable Long articleId) {

        return articleService.deleteArticle(articleId);
    }


    @Operation(summary = "Post a comment", description = "Add a comment to the article using the articleId")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "comment posted successfully",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping("articles/{articleId}/comment")
    public ResponseEntity<ApiResponse<DataArticleCommentResponse>> addComment(@PathVariable Long articleId, @RequestBody CommentDto commentDto) {

        return articleCommentService.saveComment(articleId, commentDto);
    }


    @Operation(summary = "Get article by ID", description = "Retrieve an article's details using its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Article found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("articles/{articleId}")
    public ResponseEntity<ApiResponse<DataViewArticleResponse<List<CommentItemsDto>>>> viewArticle(@PathVariable Long articleId) {
        return articleService.getArticleAndCommentById(articleId);
    }
}
