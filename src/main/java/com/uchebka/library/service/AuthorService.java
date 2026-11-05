package com.uchebka.library.service;

import com.uchebka.library.domain.Author;
import com.uchebka.library.exception.ResourceNotFoundException;
import com.uchebka.library.repository.AuthorRepository;
import com.uchebka.library.repository.BookRepository;
import com.uchebka.library.web.dto.AuthorResponse;
import com.uchebka.library.web.dto.CreateAuthorRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Правила вокруг авторов. Контроллер сюда отдаёт уже разобранный запрос
 * и получает готовый ответ.
 *
 * <p>{@code readOnly = true} на классе годится для чтения. Метод записи помечен
 * своим {@code @Transactional}: у него режим записи, он перекрывает режим класса.
 * Аннотация срабатывает, когда метод вызывают из другого бина. Вызов
 * {@code this.create(...)} внутри этого же класса транзакцию не откроет.
 */
@Service
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authors;
    private final BookRepository books;

    public AuthorService(AuthorRepository authors, BookRepository books) {
        this.authors = authors;
        this.books = books;
    }

    public List<AuthorResponse> findAll() {
        return authors.findAll().stream()
                .map(author -> AuthorResponse.of(author, books.countByAuthorId(author.getId())))
                .toList();
    }

    public AuthorResponse findById(Long id) {
        Author author = authors.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Автора с id=" + id + " в избе нет. Хоть обыщи сени, чулан и подпол."));
        return AuthorResponse.of(author, books.countByAuthorId(id));
    }

    @Transactional
    public AuthorResponse create(CreateAuthorRequest request) {
        Author saved = authors.save(new Author(request.name().trim(), request.birthYear()));
        return AuthorResponse.of(saved, 0);
    }
}
