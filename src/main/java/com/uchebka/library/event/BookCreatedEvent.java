package com.uchebka.library.event;

/** Сообщение «книга создана». Издатель не знает, кто его слушает. */
public record BookCreatedEvent(Long bookId, String title) {
}
