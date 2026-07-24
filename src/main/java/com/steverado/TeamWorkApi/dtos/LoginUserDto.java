package com.steverado.TeamWorkApi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginUserDto {

    @NotBlank(message = "email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "password cannot be empty")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
