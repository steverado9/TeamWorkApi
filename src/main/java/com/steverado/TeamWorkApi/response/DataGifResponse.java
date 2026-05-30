package com.steverado.TeamWorkApi.response;

import java.time.LocalDateTime;

public class DataGifResponse {

    private Long gifId;

    private String message;

    private LocalDateTime createdOn;

    private String title;

    private String imageUrl;

    public Long getGifId() {
        return gifId;
    }

    public void setGifId(Long gifId) {
        this.gifId = gifId;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
