package xyz.ypmngr.controller.advice;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.ypmngr.exception.BadRequestException;
import xyz.ypmngr.exception.GoogleRespondedErrorException;
import xyz.ypmngr.exception.NotFoundException;
import xyz.ypmngr.model.ErrorResponse;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException e) {
        log.error("Exception occurred", e);
        var error = new ErrorResponse()
                .code("Unexpected error")
                .message(e.getMessage());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(GoogleRespondedErrorException.class)
    public ResponseEntity<ErrorResponse> handleGoogleError(GoogleRespondedErrorException e) {
        if (e.getCause() instanceof GoogleJsonResponseException ge)
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new ErrorResponse().code("Google API returned error").message(ge.getDetails().getMessage()));

        var error = new ErrorResponse()
                .code("Unknown error from Google server")
                .message(e.getMessage());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        var error = new ErrorResponse()
                .code("Entity not found")
                .message(e.getMessage());
        return ResponseEntity.status(GONE).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        var error = new ErrorResponse()
                .code("Bad request")
                .message(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleRequestParsing(HttpMessageNotReadableException e) {
        var error = new ErrorResponse()
                .code("Request can't be parsed")
                .message(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }
}
