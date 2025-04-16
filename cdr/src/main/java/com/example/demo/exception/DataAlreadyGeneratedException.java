package com.example.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataAlreadyGeneratedException extends Exception{
    HttpStatus httpStatus;
    public DataAlreadyGeneratedException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }
}
