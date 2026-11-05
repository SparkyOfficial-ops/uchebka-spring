package com.uchebka.library.repository;

import com.uchebka.library.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Имя метода — это запрос.
 * {@code findByTitleContainingIgnoreCase} становится условием
 * «title содержит строку без учёта регистра».
 *
 * <p>{@code @EntityGraph} сразу делает JOIN на автора, чтобы потом
 * {@code book.getAuthor().getName()} не пошёл в базу отдельным запросом на каждую книгу.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = "author")
    Page<Book> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "author")
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = "author")
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = "author")
    List<Book> findByAuthorId(Long authorId);

    long countByAuthorId(Long authorId);

    boolean existsByIsbn(String isbn);

    /**
     * JPQL, не SQL: {@code Book} — имя класса, {@code b.title} — имя поля.
     * {@code join fetch} загружает автора тем же запросом.
     */
    @Query("select b from Book b join fetch b.author where b.copies = 0")
    List<Book> findOutOfStock();
}
