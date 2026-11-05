package com.uchebka.library.exception;

/**
 * Запрос составлен нормально, но текущие данные его не пропускают:
 * лимит книг у автора или повтор ISBN. Снаружи это ответ 409.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
