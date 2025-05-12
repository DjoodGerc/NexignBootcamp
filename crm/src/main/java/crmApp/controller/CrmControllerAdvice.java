package crmApp.controller;

import crmApp.exception.ClientException;
import crmApp.exception.TariffIsNotActiveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CrmControllerAdvice {
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> handleClientException(ClientException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(TariffIsNotActiveException.class)
    public ResponseEntity<String> handleTariffNotActivatedException(ClientException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
