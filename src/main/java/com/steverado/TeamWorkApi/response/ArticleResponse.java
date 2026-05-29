package com.steverado.TeamWorkApi.response;

public class ArticleResponse<T> {

    private String title;

    private String article;

    private String status;

    private T data;

    public ArticleResponse() {
    }

    public ArticleResponse(String title, String article, String status, T data) {
        this.title = title;
        this.article = article;
        this.status = status;
        this.data = data;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
