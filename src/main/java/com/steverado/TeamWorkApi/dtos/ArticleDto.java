package com.steverado.TeamWorkApi.dtos;

import com.steverado.TeamWorkApi.entity.Article;

public class ArticleDto {

    private String title;

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
