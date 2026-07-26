package com.steverado.TeamWorkApi.exceptions;

public class NotAdminException extends RuntimeException {

    public NotAdminException(String message) {
        super(message);
    }
}
