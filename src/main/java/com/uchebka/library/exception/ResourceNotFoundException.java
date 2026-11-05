package com.uchebka.library.exception;

/** В базе нет объекта, который просит клиент. Снаружи это ответ 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
