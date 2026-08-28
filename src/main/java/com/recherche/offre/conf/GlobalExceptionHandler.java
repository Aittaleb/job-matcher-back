package com.recherche.offre.conf;

import com.recherche.offre.dto.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidationError(final MethodArgumentNotValidException exception,
                                                             final HttpServletRequest request) {
        final Map<String, String> details = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                details.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));

        final ApiErrorDto body = buildError(
                HttpStatus.BAD_REQUEST,
                "Validation des donnees echouee",
                request,
                details
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDto> handleConstraintViolation(final ConstraintViolationException exception,
                                                                 final HttpServletRequest request) {
        final Map<String, String> details = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                details.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage()));

        final ApiErrorDto body = buildError(
                HttpStatus.BAD_REQUEST,
                "Validation des parametres echouee",
                request,
                details
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDto> handleResponseStatusException(final ResponseStatusException exception,
                                                                     final HttpServletRequest request) {
        final ApiErrorDto body = buildError(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                exception.getReason(),
                request,
                null
        );

        return ResponseEntity.status(exception.getStatusCode()).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDto> handleIllegalArgumentException(final IllegalArgumentException exception,
                                                                      final HttpServletRequest request) {
        final ApiErrorDto body = buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request, null);
        return ResponseEntity.badRequest().body(body);
    }

    private ApiErrorDto buildError(final HttpStatus status,
                                   final String message,
                                   final HttpServletRequest request,
                                   final Map<String, String> details) {
        return new ApiErrorDto()
                .setTimestamp(Instant.now())
                .setStatus(status.value())
                .setError(status.getReasonPhrase())
                .setMessage(message)
                .setPath(request.getRequestURI())
                .setDetails(details);
    }
}

