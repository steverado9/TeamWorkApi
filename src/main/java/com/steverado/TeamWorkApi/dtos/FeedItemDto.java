package com.steverado.TeamWorkApi.dtos;

import java.time.LocalDateTime;

public class FeedItemDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdOn;
    private Long authorId;

    public FeedItemDto() {
    }

    public FeedItemDto(Long id, String title, String content, LocalDateTime createdOn, Long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
