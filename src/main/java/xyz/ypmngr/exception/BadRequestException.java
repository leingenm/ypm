package xyz.ypmngr.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(String.format("Wrong input. %s", message));
    }

    public BadRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
