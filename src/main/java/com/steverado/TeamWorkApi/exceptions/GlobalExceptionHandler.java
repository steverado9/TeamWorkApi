package com.steverado.TeamWorkApi.exceptions;

import com.steverado.TeamWorkApi.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    String error = "error";

    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<ApiResponse> handleNotAdmin(NotAdminException exception) {

        ApiResponse response = new ApiResponse<>(error, "FORBIDDEN!");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        ApiResponse response = new ApiResponse<>(error, "fields cannot be empty");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException exception) {

        ApiResponse response = new ApiResponse<>(error, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ApiResponse> handleArticleNotFound(ArticleNotFoundException exception) {

        ApiResponse response  = new ApiResponse(error, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(GifNotFoundException.class)
    public ResponseEntity<ApiResponse> handleGifNotFound(GifNotFoundException exception) {

        ApiResponse response = new ApiResponse<>(error, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MissingServletRequestPartException exception) {

        ApiResponse response = new ApiResponse<>(error, "file is not present");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleSecureException(Exception exception) {

        ApiResponse response = null;

        //print error in the console
        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {

             response = new ApiResponse(error, "The username or password is incorrect");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (exception instanceof AccountStatusException) {

            response = new ApiResponse(error, "The account is locked");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        }

        if (exception instanceof AccessDeniedException) {

            response = new ApiResponse(error, "You are not authorized to access this resource");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        }

        if (exception instanceof SignatureException) {

            response = new ApiResponse(error, "The JWT signature is invalid");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        }

        if (exception instanceof ExpiredJwtException) {

             response  = new ApiResponse(error, "The JWT token has expired");

             return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        }

        if (response == null) {

            response  = new ApiResponse(error, "Unknown internal server error.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
