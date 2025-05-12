package brtApp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
public class TarifficationException extends RuntimeException {
    HttpStatusCode httpStatusCode;
    HttpStatus httpStatus;

    public TarifficationException(String message) {
        super(message);
    }

    public TarifficationException(HttpStatusCode status, String message) {
        super(message);
        this.httpStatusCode = status;
    }
}
