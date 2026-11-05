package com.uchebka.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Строка таблицы {@code authors}.
 *
 * <p>Пустой конструктор обязателен: Hibernate создаёт объект сам, без нашего конструктора
 * с именем и годом, а потом заполняет поля.
 *
 * <p>Сущность наружу через контроллер не отдаём. Её форма удобна базе, а клиенту нужен DTO.
 */
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "birth_year", nullable = false)
    private Integer birthYear;

    /**
     * Обратная сторона связи. Внешний ключ хранится в таблице books, в колонке author_id.
     * Хозяин связи — поле {@link Book#author}.
     */
    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    protected Author() {
    }

    public Author(String name, Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "Author{id=" + id + ", name='" + name + "'}";
    }
}
