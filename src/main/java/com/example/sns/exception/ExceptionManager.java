package com.example.sns.exception;


import com.example.sns.controller.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Optional;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(SpringBootAppException.class)
    public ResponseEntity<?> springBootAppException(SpringBootAppException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(RsData.error(new ErrorResult(e.getErrorCode(), e.getMessage())));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException e) {
        ErrorResult errorResult = new ErrorResult(ErrorCode.DATABASE_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RsData.error(new ErrorResult(errorResult.getErrorCode(), errorResult.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        String message = Optional.ofNullable(e.getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RsData.error(message));
    }

}
