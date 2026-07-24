package com.steverado.TeamWorkApi.dtos;

import jakarta.validation.constraints.NotBlank;

public class CommentDto {

    @NotBlank(message = "comment field should not be empty")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
