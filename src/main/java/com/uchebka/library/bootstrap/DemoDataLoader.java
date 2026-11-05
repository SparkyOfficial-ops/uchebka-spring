package com.uchebka.library.bootstrap;

import com.uchebka.library.repository.AuthorRepository;
import com.uchebka.library.service.AuthorService;
import com.uchebka.library.service.BookService;
import com.uchebka.library.web.dto.AuthorResponse;
import com.uchebka.library.web.dto.CreateAuthorRequest;
import com.uchebka.library.web.dto.CreateBookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Код после полного старта контейнера. К этому моменту бины, таблицы и Tomcat уже готовы.
 *
 * <p>Данные кладутся через сервисы, тем же путём, что и запросы с сайта:
 * сработают лимит, транзакция и событие. В логе будет по колоколу на каждую книгу.
 *
 * <p>База в памяти пустая при каждом запуске, поэтому проверка {@code count}
 * нужна в основном тестам: они поднимают контекст один раз и вызывают сценарии повторно.
 * Для настоящих миграций схемы позже смотрят Flyway или Liquibase, не этот класс.
 */
@Component
public class DemoDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataLoader.class);

    private final AuthorRepository authors;
    private final AuthorService authorService;
    private final BookService bookService;

    public DemoDataLoader(AuthorRepository authors, AuthorService authorService, BookService bookService) {
        this.authors = authors;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (authors.count() > 0) {
            return;
        }

        AuthorResponse tolstoy = author("Лев Толстой", 1828);
        book(tolstoy, "Война и мир", 1869, "9785171200001", 4);
        book(tolstoy, "Детство", 1852, "9785171200002", 0);

        book(author("Фёдор Достоевский", 1821), "Преступление и наказание", 1866, "9785171200003", 2);
        book(author("Александр Пушкин", 1799), "Евгений Онегин", 1833, "9785171200004", 3);
        book(author("Николай Гоголь", 1809), "Мёртвые души", 1842, "9785171200005", 3);
        book(author("Антон Чехов", 1860), "Вишнёвый сад", 1904, "9785171200006", 2);
        book(author("Михаил Булгаков", 1891), "Мастер и Маргарита", 1967, "9785171200007", 6);
        book(author("Михаил Лермонтов", 1814), "Герой нашего времени", 1840, "9785171200008", 2);
        book(author("Иван Тургенев", 1818), "Отцы и дети", 1862, "9785171200009", 3);
        book(author("Иван Гончаров", 1812), "Обломов", 1859, "9785171200010", 1);
        book(author("Александр Грибоедов", 1795), "Горе от ума", 1825, "9785171200011", 2);
        book(author("Александр Островский", 1823), "Гроза", 1859, "9785171200012", 2);
        book(author("Михаил Салтыков-Щедрин", 1826), "История одного города", 1870, "9785171200013", 1);
        book(author("Николай Некрасов", 1821), "Кому на Руси жить хорошо", 1877, "9785171200014", 2);
        book(author("Денис Фонвизин", 1745), "Недоросль", 1782, "9785171200015", 1);
        book(author("Михаил Шолохов", 1905), "Тихий Дон", 1928, "9785171200016", 4);
        book(author("Анна Ахматова", 1889), "Реквием", 1963, "9785171200017", 1);
        book(author("Александр Блок", 1880), "Двенадцать", 1918, "9785171200018", 2);
        book(author("Сергей Есенин", 1895), "Анна Снегина", 1925, "9785171200019", 1);
        book(author("Владимир Маяковский", 1893), "Облако в штанах", 1915, "9785171200020", 3);

        log.info("Амбар полон: двадцать томов классики. У Льва Николаевича лимит уже занят. Чай разливай.");
    }

    private AuthorResponse author(String name, int birthYear) {
        return authorService.create(new CreateAuthorRequest(name, birthYear));
    }

    private void book(AuthorResponse author, String title, int year, String isbn, int copies) {
        bookService.create(new CreateBookRequest(title, year, isbn, copies, author.id()));
    }
}
