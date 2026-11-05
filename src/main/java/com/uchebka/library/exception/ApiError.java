package com.uchebka.library.exception;

import java.time.Instant;
import java.util.Map;

/** Одинаковая форма ошибки для всех ответов из GlobalExceptionHandler. */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fields
) {
}
