package com.uchebka.library.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель. {@code BookService} его не вызывает: он публикует событие в контейнер,
 * а контейнер находит все методы {@code @EventListener} с подходящим аргументом.
 *
 * <p>Такой слушатель выполняется сразу, в той же транзакции, ещё до фиксации записи.
 * Реакцию «после успешного commit» вешают через {@code @TransactionalEventListener}.
 */
@Component
public class LibraryEventListener {

    private static final Logger log = LoggerFactory.getLogger(LibraryEventListener.class);

    @EventListener
    public void onBookCreated(BookCreatedEvent event) {
        log.info("Бам, в колокол! BookCreatedEvent: id={}, том «{}»", event.bookId(), event.title());
    }
}
