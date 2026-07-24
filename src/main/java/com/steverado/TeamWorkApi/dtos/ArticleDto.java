package com.steverado.TeamWorkApi.dtos;

import com.steverado.TeamWorkApi.entity.Article;
import jakarta.validation.constraints.NotBlank;

public class ArticleDto {

    @NotBlank(message = "title field should not be empty")
    private String title;

    @NotBlank(message = "content field should not be empty")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
