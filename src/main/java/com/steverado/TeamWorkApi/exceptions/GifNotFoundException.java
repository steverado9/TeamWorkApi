package com.steverado.TeamWorkApi.exceptions;

public class GifNotFoundException extends RuntimeException {

    public GifNotFoundException(String message) {
        super(message);
    }
}
