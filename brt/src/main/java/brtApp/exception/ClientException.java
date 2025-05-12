package brtApp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientException extends RuntimeException {
    HttpStatusCode httpStatusCode;
    HttpStatus httpStatus;

    public ClientException(String message) {
        super(message);
    }

    public ClientException(HttpStatusCode status, String message) {
        super(message);
        this.httpStatusCode = status;
    }

}
