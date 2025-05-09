package crmApp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Data
public class ClientException extends RuntimeException {
    HttpStatusCode httpStatusCode;
    HttpStatus httpStatus;
    public ClientException(String message) {
        super(message);
    }
    public ClientException(HttpStatusCode status, String message) {
        super(message);
        this.httpStatusCode=status;
    }

}
