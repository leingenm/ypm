package xyz.ypmngr.dto.response;

import java.time.Instant;

public record ExceptionResponse(int code, String message, Instant date) {
}
