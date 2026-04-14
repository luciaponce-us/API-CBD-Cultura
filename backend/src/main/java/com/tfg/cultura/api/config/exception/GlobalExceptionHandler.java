package com.tfg.cultura.api.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger("appLogger");

    private final ApiErrorBuilder apiErrorBuilder;

    public GlobalExceptionHandler(ApiErrorBuilder apiErrorBuilder) {
        this.apiErrorBuilder = apiErrorBuilder;
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> "Campo " + error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(". "));

        return apiErrorBuilder.build(ex,HttpStatus.BAD_REQUEST,"Errores de validación",logger,message);
    }

    
    

}
