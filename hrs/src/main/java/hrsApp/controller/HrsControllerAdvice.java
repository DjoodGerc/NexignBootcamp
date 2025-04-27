package hrsApp.controller;

import hrsApp.exception.ClientException;
import hrsApp.exception.MonthTarifficationIsNotAllowedForEventTariffException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class HrsControllerAdvice {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> handleClientExceptionException(ClientException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(MonthTarifficationIsNotAllowedForEventTariffException.class)
    public ResponseEntity<String> handleMonthTarifficationIsNotAllowedForEventException(MonthTarifficationIsNotAllowedForEventTariffException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundExceptionException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
