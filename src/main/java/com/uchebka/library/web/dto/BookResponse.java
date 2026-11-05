package com.uchebka.library.web.dto;

import com.uchebka.library.domain.Book;

public record BookResponse(
        Long id,
        String title,
        Integer publishedYear,
        String isbn,
        Integer copies,
        Long authorId,
        String authorName
) {

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getPublishedYear(),
                book.getIsbn(),
                book.getCopies(),
                book.getAuthor().getId(),
                book.getAuthor().getName());
    }
}
