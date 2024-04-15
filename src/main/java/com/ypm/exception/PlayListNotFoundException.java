package com.ypm.exception;

public class PlayListNotFoundException extends RuntimeException {

    public PlayListNotFoundException(String identifier, String message) {
        super(String.format("Playlist with the '%s' identifier was not found. %s", identifier, message));
    }
}
