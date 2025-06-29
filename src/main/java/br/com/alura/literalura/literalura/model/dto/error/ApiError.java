package br.com.alura.literalura.literalura.model.dto.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        int statusCode,
        String status,
        String message,
        LocalDateTime timestamp
) {
    public ApiError(HttpStatus httpStatus, String message) {
        this(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
    }
}