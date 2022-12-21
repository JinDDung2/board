package com.example.sns.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(SpringBootAppException.class)
    public ResponseEntity<?> springBootAppException(SpringBootAppException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(e.getMessage());
    }

}
