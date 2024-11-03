package com.ypm.error;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.ypm.dto.response.ExceptionResponse;
import com.ypm.exception.PlayListNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.Instant;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public GlobalExceptionHandler() {
        super();
    }

    @ExceptionHandler({GoogleJsonResponseException.class})
    public ResponseEntity<?> handleBadRequest(final GoogleJsonResponseException ex, final WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getStatusCode(), ex.getDetails().getMessage(),
                                                                    Instant.now());

        return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), valueOf(ex.getStatusCode()), request);
    }

    @ExceptionHandler({PlayListNotFoundException.class})
    public ResponseEntity<?> handleBadRequest(final PlayListNotFoundException ex, final WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(NOT_FOUND.value(), ex.getMessage(), Instant.now());

        return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<?> handleInternal(final IOException ex, final WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
                                                                    Instant.now());

        return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntime(final RuntimeException ex, final WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
                                                                    Instant.now());

        return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }
}
