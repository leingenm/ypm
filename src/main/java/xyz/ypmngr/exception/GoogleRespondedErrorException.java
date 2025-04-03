package xyz.ypmngr.exception;

public class GoogleRespondedErrorException extends RuntimeException {

    public GoogleRespondedErrorException(Throwable throwable) {
        super("Error received on request to Google API", throwable);
    }
}
