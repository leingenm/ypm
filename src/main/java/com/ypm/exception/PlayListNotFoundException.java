package com.ypm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayListNotFoundException extends RuntimeException {

    public PlayListNotFoundException(String identifier, String message) {
        super(String.format("Playlist with %s identifier was not found %n%s%n", identifier, message));
    }
}
