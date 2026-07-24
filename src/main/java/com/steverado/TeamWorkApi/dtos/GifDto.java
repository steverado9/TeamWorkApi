package com.steverado.TeamWorkApi.dtos;

import jakarta.validation.constraints.NotBlank;

public class GifDto {

    @NotBlank(message = "title field should not be empty")
    private String title;

//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
