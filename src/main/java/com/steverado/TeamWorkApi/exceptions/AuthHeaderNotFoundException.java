package com.steverado.TeamWorkApi.exceptions;

public class AuthHeaderNotFoundException extends RuntimeException{
    public AuthHeaderNotFoundException(String message) {
        super(message);
    }
}
