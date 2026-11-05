package com.uchebka.library.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uchebka.library.web.dto.AppInfoResponse;
import com.uchebka.library.web.dto.AuthorResponse;
import com.uchebka.library.web.dto.BookResponse;
import com.uchebka.library.web.dto.CreateAuthorRequest;
import com.uchebka.library.web.dto.CreateBookRequest;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Поднимает настоящее приложение на случайном порту: фильтр безопасности, контроллеры,
 * сервисы, H2 и демо-данные. Медленнее модульного теста, зато проверяет сборку целиком.
 *
 * <p>{@code @AutoConfigureTestRestTemplate} даёт HTTP-клиент, который сам знает порт теста.
 * Он не бросает исключение на 400 и 401, а возвращает ответ как есть.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class LibraryApiTest {

    @Autowired
    TestRestTemplate rest;

    @Test
    void infoShowsDefaultNameAndBothGreetings() {
        ResponseEntity<AppInfoResponse> response = rest.getForEntity("/api/info", AppInfoResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AppInfoResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Изба-читальня «У Лукоморья»", body.applicationName());
        assertEquals("Привет, родной! Проходи, не стой на пороге: самовар уже пыхтит.", body.greeting());
        assertEquals(
                "Здравствуйте, многоуважаемый. Честь имею кланяться и подать том в твёрдом переплёте.",
                body.formalGreeting());
    }

    @Test
    void seededBooksArePublic() {
        ResponseEntity<String> response = rest.getForEntity("/api/books", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Война и мир"));
        assertTrue(response.getBody().contains("Детство"));
    }

    @Test
    void outOfStockContainsOnlyEmptyShelf() {
        ResponseEntity<String> response = rest.getForEntity("/api/books/out-of-stock", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Детство"));
        assertFalse(response.getBody().contains("Война и мир"));
    }

    @Test
    void missingBookAndAuthorAre404() {
        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity("/api/books/99999", String.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity("/api/authors/99999/books", String.class).getStatusCode());
    }

    @Test
    void postWithoutPasswordIs401() {
        ResponseEntity<String> response = rest.postForEntity(
                "/api/authors",
                new CreateAuthorRequest("Без пароля", 1900),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void blankNameIs400() {
        TestRestTemplate student = rest.withBasicAuth("student", "student");

        ResponseEntity<String> response = student.postForEntity(
                "/api/authors",
                new CreateAuthorRequest("   ", 1900),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Имя не может быть пустым"));
    }

    @Test
    void tolstoyIsAtTheLimit() {
        AuthorResponse tolstoy = findAuthor("Толстой");
        TestRestTemplate student = rest.withBasicAuth("student", "student");

        ResponseEntity<String> response = student.postForEntity(
                "/api/books",
                new CreateBookRequest("Анна Каренина", 1877, "9785171200099", 1, tolstoy.id()),
                String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Лимит"));
    }

    @Test
    void studentCanAddBookForPushkin() {
        AuthorResponse pushkin = findAuthor("Пушкин");
        TestRestTemplate student = rest.withBasicAuth("student", "student");

        ResponseEntity<BookResponse> response = student.postForEntity(
                "/api/books",
                new CreateBookRequest("Капитанская дочка", 1836, "9785171200088", 1, pushkin.id()),
                BookResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Капитанская дочка", response.getBody().title());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().getPath().contains("/api/books/"));
    }

    private AuthorResponse findAuthor(String partOfName) {
        ResponseEntity<AuthorResponse[]> response = rest.getForEntity("/api/authors", AuthorResponse[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthorResponse[] authors = response.getBody();
        assertNotNull(authors);
        return Arrays.stream(authors)
                .filter(author -> author.name().contains(partOfName))
                .findFirst()
                .orElseThrow();
    }
}
