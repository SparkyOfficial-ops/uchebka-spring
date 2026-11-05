package com.uchebka.library.web;

import com.uchebka.library.service.BookService;
import com.uchebka.library.web.dto.BookResponse;
import com.uchebka.library.web.dto.CreateBookRequest;
import com.uchebka.library.web.dto.UpdateBookRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Маршруты книг. Более конкретный путь {@code /out-of-stock} объявлен раньше
 * {@code /{id}}: иначе слово out-of-stock попало бы в переменную id и сломало разбор числа.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * {@code Pageable} Spring собирает сам из query-параметров {@code page}, {@code size} и {@code sort}.
     * Пример: {@code /api/books?title=мир&page=0&size=10&sort=title}.
     */
    @GetMapping
    public Page<BookResponse> list(
            @RequestParam(required = false) String title,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return bookService.findAll(title, pageable);
    }

    @GetMapping("/out-of-stock")
    public List<BookResponse> outOfStock() {
        return bookService.findOutOfStock();
    }

    @GetMapping("/{id}")
    public BookResponse one(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody CreateBookRequest request) {
        BookResponse created = bookService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
