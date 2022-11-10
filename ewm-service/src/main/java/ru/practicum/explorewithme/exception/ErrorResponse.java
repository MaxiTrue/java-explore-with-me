package ru.practicum.explorewithme.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponse {

    private final List<String> errors;
    private final String status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;


    public ErrorResponse(List<String> errors, String status, String reason, String message, LocalDateTime timestamp) {
        this.errors = errors;
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String status, String reason, String message, LocalDateTime timestamp) {
        this.errors = null;
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}
