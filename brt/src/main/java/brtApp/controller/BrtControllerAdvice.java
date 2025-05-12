package brtApp.controller;

import brtApp.exception.EntityAlreadyExsistsException;
import brtApp.exception.TarifficationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class BrtControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExsistsException.class)
    public ResponseEntity<String> handleAlreadyExistsException(EntityAlreadyExsistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TarifficationException.class)
    public ResponseEntity<String> handleTarifficationException(TarifficationException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }
}