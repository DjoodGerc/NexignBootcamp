package brtApp.exception;

public class EntityAlreadyExsistsException extends RuntimeException {
    public EntityAlreadyExsistsException(String message) {
        super(message);
    }
}
