package com.uchebka.library.service;

import com.uchebka.library.domain.Author;
import com.uchebka.library.domain.Book;
import com.uchebka.library.event.BookCreatedEvent;
import com.uchebka.library.exception.BusinessException;
import com.uchebka.library.exception.ResourceNotFoundException;
import com.uchebka.library.config.LibraryProperties;
import com.uchebka.library.repository.AuthorRepository;
import com.uchebka.library.repository.BookRepository;
import com.uchebka.library.web.dto.BookResponse;
import com.uchebka.library.web.dto.CreateBookRequest;
import com.uchebka.library.web.dto.UpdateBookRequest;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Правила библиотеки живут здесь, не в контроллере и не в сущности.
 * Конструктор — единственный. Spring видит его и подставляет все аргументы сам,
 * писать {@code @Autowired} не нужно.
 */
@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository books;
    private final AuthorRepository authors;
    private final ApplicationEventPublisher events;
    private final LibraryProperties properties;

    public BookService(
            BookRepository books,
            AuthorRepository authors,
            ApplicationEventPublisher events,
            LibraryProperties properties) {
        this.books = books;
        this.authors = authors;
        this.events = events;
        this.properties = properties;
    }

    public Page<BookResponse> findAll(String title, Pageable pageable) {
        Page<Book> page = title == null || title.isBlank()
                ? books.findAll(pageable)
                : books.findByTitleContainingIgnoreCase(title.trim(), pageable);
        return page.map(BookResponse::from);
    }

    public List<BookResponse> findOutOfStock() {
        return books.findOutOfStock().stream().map(BookResponse::from).toList();
    }

    /**
     * Пустой список значит «автор есть, книг нет».
     * Неизвестный автор — это 404, а не пустой список.
     */
    public List<BookResponse> findByAuthor(Long authorId) {
        if (!authors.existsById(authorId)) {
            throw new ResourceNotFoundException(
                    "Автора с id=" + authorId + " в избе нет. Хоть обыщи сени, чулан и подпол.");
        }
        return books.findByAuthorId(authorId).stream().map(BookResponse::from).toList();
    }

    public BookResponse findById(Long id) {
        return books.findById(id)
                .map(BookResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книги с id=" + id + " на полке нет. Видать, ещё при царе Горохе выдали и не вернули."));
    }

    @Transactional
    public BookResponse create(CreateBookRequest request) {
        Author author = authors.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Автора с id=" + request.authorId() + " в избе нет. Хоть обыщи сени, чулан и подпол."));

        long already = books.countByAuthorId(author.getId());
        int limit = properties.getMaxBooksPerAuthor();
        if (already >= limit) {
            throw new BusinessException(
                    "У автора «" + author.getName() + "» уже " + already
                            + " книг, полка трещит по шву. Лимит — " + limit
                            + ". Указ лежит в application.yml: library.max-books-per-author.");
        }
        if (books.existsByIsbn(request.isbn())) {
            throw new BusinessException(
                    "Книга с ISBN " + request.isbn() + " уже есть. Второй раз в амбарную книгу не пишем.");
        }

        Book saved = books.save(new Book(
                request.title().trim(),
                request.publishedYear(),
                request.isbn(),
                request.copies(),
                author));
        events.publishEvent(new BookCreatedEvent(saved.getId(), saved.getTitle()));
        return BookResponse.from(saved);
    }

    @Transactional
    public BookResponse update(Long id, UpdateBookRequest request) {
        Book book = books.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книги с id=" + id + " на полке нет. Видать, ещё при царе Горохе выдали и не вернули."));
        book.setTitle(request.title().trim());
        book.setPublishedYear(request.publishedYear());
        book.setCopies(request.copies());
        // Отдельный save не нужен: книга уже в сессии, Hibernate запишет UPDATE в конце транзакции.
        return BookResponse.from(book);
    }

    @Transactional
    public void delete(Long id) {
        if (!books.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Книги с id=" + id + " на полке нет. Видать, ещё при царе Горохе выдали и не вернули.");
        }
        books.deleteById(id);
    }
}
