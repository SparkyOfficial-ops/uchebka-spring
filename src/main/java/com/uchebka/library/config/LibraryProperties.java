package com.uchebka.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки с префиксом {@code library} из {@code application.yml}.
 *
 * <p>Класс обычный: на нём нет {@code @Component}. Бином его делает
 * {@code @ConfigurationPropertiesScan} на классе приложения. Spring создаёт объект
 * и заполняет поля. Имена связываются мягко: {@code max-books-per-author} в yaml
 * попадает в {@code maxBooksPerAuthor}.
 *
 * <p>Значения в полях — запасные. Файл настроек их перекрывает.
 */
@ConfigurationProperties(prefix = "library")
public class LibraryProperties {

    private String name = "Изба-читальня «У Лукоморья»";
    private int maxBooksPerAuthor = 2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxBooksPerAuthor() {
        return maxBooksPerAuthor;
    }

    public void setMaxBooksPerAuthor(int maxBooksPerAuthor) {
        this.maxBooksPerAuthor = maxBooksPerAuthor;
    }
}
