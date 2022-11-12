package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> objectNotFoundException(final ObjectNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                new ArrayList<>(),
                "NOT_FOUND",
                "The required object was not found.",
                e.getObjectName() + " with id=" + e.getObjectId() + " was not found.",
                LocalDateTime.now());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidException.class})
    public ResponseEntity<Object> validException(final Exception e) {

        ErrorResponse errorResponse = new ErrorResponse(
                new ArrayList<>(),
                "FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
