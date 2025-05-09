package crmApp.exception;

public class TariffIsNotActiveException extends RuntimeException {
    public TariffIsNotActiveException(String message) {
        super(message);
    }
}
