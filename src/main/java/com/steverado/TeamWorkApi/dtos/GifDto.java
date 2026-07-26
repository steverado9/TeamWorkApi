package com.steverado.TeamWorkApi.dtos;

import jakarta.validation.constraints.NotBlank;

public class GifDto {

    @NotBlank(message = "title field should not be empty")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
