package hrsApp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MonthTarifficationIsNotAllowedForEventTariffException extends Exception {
    HttpStatus status;
    public MonthTarifficationIsNotAllowedForEventTariffException(HttpStatus status,String message)
    {
        super(message);
        this.status=status;
    }
}
