package com.uchebka.library.web;

import com.uchebka.library.service.AuthorService;
import com.uchebka.library.service.BookService;
import com.uchebka.library.web.dto.AuthorResponse;
import com.uchebka.library.web.dto.BookResponse;
import com.uchebka.library.web.dto.CreateAuthorRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * HTTP-слой автора. Здесь нет правил «сколько книг можно» и нет обращения к базе:
 * контроллер переводит запрос в вызов сервиса и ответ в JSON.
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public List<AuthorResponse> list() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public AuthorResponse one(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @GetMapping("/{id}/books")
    public List<BookResponse> books(@PathVariable Long id) {
        return bookService.findByAuthor(id);
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody CreateAuthorRequest request) {
        AuthorResponse created = authorService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}
