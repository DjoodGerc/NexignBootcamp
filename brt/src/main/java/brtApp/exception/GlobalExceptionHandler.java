package brtApp.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(TooEarlyForTarifficationException.class)
    public void handleMonthlyTarifficationException(TooEarlyForTarifficationException ex){
        log.info(ex.getMessage());
    }
    @ExceptionHandler(NotRomashkaException.class)
    public void handleNotRomashkaException(NotRomashkaException ex){
        log.warn(ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public void handleEntityNotFoundException(EntityNotFoundException ex){
        log.error(ex.getMessage());
    }
}
