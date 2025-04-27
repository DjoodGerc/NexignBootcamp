package hrsApp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ClientException extends RuntimeException {
    HttpStatus status;
    public ClientException(HttpStatus status,String message) {
        super(message);
        this.status=status;
    }
}
