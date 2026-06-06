package com.steverado.TeamWorkApi.dtos;

public class ArticleCommentItemsDto {
    private Long commentId;
    private String comment;
    private Long authorId;

    public ArticleCommentItemsDto() {
    }

    public ArticleCommentItemsDto(Long commentId, String comment, Long authorId) {
        this.commentId = commentId;
        this.comment = comment;
        this.authorId = authorId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
