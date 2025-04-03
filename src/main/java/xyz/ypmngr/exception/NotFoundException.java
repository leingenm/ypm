package xyz.ypmngr.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String identifier, String message) {
        super(String.format("Entity with the '%s' identifier was not found. %s", identifier,
            message));
    }
}
