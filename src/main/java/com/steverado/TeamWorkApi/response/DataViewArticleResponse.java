package com.steverado.TeamWorkApi.response;

import java.time.LocalDateTime;

public class DataViewArticleResponse<T> {

    private Long id;
    private String title;
    private String article;
    private LocalDateTime createdOn;
    private T comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public T getComments() {
        return comments;
    }

    public void setComments(T comments) {
        this.comments = comments;
    }
}
