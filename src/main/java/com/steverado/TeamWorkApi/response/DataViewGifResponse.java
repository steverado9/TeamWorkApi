package com.steverado.TeamWorkApi.response;

import java.time.LocalDateTime;

public class DataViewGifResponse<T> {

    private Long id;
    private String title;
    private String url;
    private LocalDateTime createdOn;
    private T comments;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public T getComments() {
        return comments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setComments(T comments) {
        this.comments = comments;
    }
}
