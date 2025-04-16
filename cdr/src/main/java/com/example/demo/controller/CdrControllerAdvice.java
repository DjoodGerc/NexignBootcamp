package com.example.demo.controller;

import com.example.demo.exception.DataAlreadyGeneratedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CdrControllerAdvice {

    @ExceptionHandler(DataAlreadyGeneratedException.class)
    public ResponseEntity<String> handleDataAlreadyGeneratedException(DataAlreadyGeneratedException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

}
