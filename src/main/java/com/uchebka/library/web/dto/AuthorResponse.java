package com.uchebka.library.web.dto;

import com.uchebka.library.domain.Author;

/** То, что клиент видит про автора. Коллекции книг сущности здесь нет. */
public record AuthorResponse(Long id, String name, Integer birthYear, long bookCount) {

    public static AuthorResponse of(Author author, long bookCount) {
        return new AuthorResponse(author.getId(), author.getName(), author.getBirthYear(), bookCount);
    }
}
