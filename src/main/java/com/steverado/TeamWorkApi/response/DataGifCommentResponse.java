package com.steverado.TeamWorkApi.response;

import java.time.LocalDateTime;

public class DataGifCommentResponse {

    private String message;

    private LocalDateTime createdOn;

    private String gifTitle;

    private String comment;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getGifTitle() {
        return gifTitle;
    }

    public void setGifTitle(String gifTitle) {
        this.gifTitle = gifTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
