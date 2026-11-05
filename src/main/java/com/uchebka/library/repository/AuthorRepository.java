package com.uchebka.library.repository;

import com.uchebka.library.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Реализацию этого интерфейса пишет Spring Data в момент старта.
 *
 * <p>{@code JpaRepository<Author, Long>} уже умеет {@code save}, {@code findById},
 * {@code findAll}, {@code count}, {@code existsById} и {@code deleteById}.
 * {@code Long} — тип первичного ключа.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
