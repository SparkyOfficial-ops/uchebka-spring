package com.uchebka.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.uchebka.library.config.LibraryProperties;
import com.uchebka.library.domain.Author;
import com.uchebka.library.domain.Book;
import com.uchebka.library.event.BookCreatedEvent;
import com.uchebka.library.exception.BusinessException;
import com.uchebka.library.exception.ResourceNotFoundException;
import com.uchebka.library.repository.AuthorRepository;
import com.uchebka.library.repository.BookRepository;
import com.uchebka.library.web.dto.BookResponse;
import com.uchebka.library.web.dto.CreateBookRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Сервис собран руками, без Spring. Так проверяют правило, а не контейнер:
 * тест делает то же, что контейнер делает в конструкторе — передаёт зависимости.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository books;

    @Mock
    AuthorRepository authors;

    @Mock
    ApplicationEventPublisher events;

    BookService service;

    @BeforeEach
    void setUp() {
        LibraryProperties properties = new LibraryProperties();
        properties.setMaxBooksPerAuthor(1);
        service = new BookService(books, authors, events, properties);
    }

    @Test
    void missingAuthorDoesNotTouchBooks() {
        when(authors.findById(9L)).thenReturn(Optional.empty());

        CreateBookRequest request = new CreateBookRequest("Книга", 2000, "9780000000001", 1, 9L);

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verifyNoInteractions(books);
    }

    @Test
    void limitRejectsTheBook() {
        Author author = new Author("Автор", 1900);
        author.setId(1L);
        when(authors.findById(1L)).thenReturn(Optional.of(author));
        when(books.countByAuthorId(1L)).thenReturn(1L);

        CreateBookRequest request = new CreateBookRequest("Книга", 2000, "9780000000002", 1, 1L);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.create(request));
        assertTrue(exception.getMessage().contains("Лимит"));
        verify(books, never()).save(any());
    }

    @Test
    void createSavesBookAndPublishesEvent() {
        Author author = new Author("Автор", 1900);
        author.setId(5L);
        when(authors.findById(5L)).thenReturn(Optional.of(author));
        when(books.countByAuthorId(5L)).thenReturn(0L);
        when(books.existsByIsbn("9780000000003")).thenReturn(false);
        when(books.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(10L);
            return book;
        });

        BookResponse response = service.create(
                new CreateBookRequest("Новая", 1999, "9780000000003", 2, 5L));

        assertEquals(10L, response.id());
        assertEquals("Автор", response.authorName());
        verify(events).publishEvent(any(BookCreatedEvent.class));
    }
}
