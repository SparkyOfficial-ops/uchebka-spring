package com.uchebka.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Строка таблицы {@code books}.
 *
 * <p>{@code LAZY} значит: автор сам по себе не читается. Hibernate подставит заглушку
 * и сходит в базу, только когда код вызовет {@code getAuthor()}. Пока открыта транзакция
 * сервиса, это сработает. После неё заглушка бросит {@code LazyInitializationException}.
 * Поэтому репозиторий заранее подгружает автора через {@code @EntityGraph},
 * а сервис собирает DTO до выхода из транзакции.
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "published_year", nullable = false)
    private Integer publishedYear;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(nullable = false)
    private Integer copies;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    protected Book() {
    }

    public Book(String title, Integer publishedYear, String isbn, Integer copies, Author author) {
        this.title = title;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.copies = copies;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public Author getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "'}";
    }
}
