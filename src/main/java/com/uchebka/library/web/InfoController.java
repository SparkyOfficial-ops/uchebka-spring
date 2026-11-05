package com.uchebka.library.web;

import com.uchebka.library.config.LibraryProperties;
import com.uchebka.library.greeting.Greeting;
import com.uchebka.library.web.dto.AppInfoResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Маленькая витрина внедрения зависимостей.
 * Убери любой {@code @Qualifier} — приложение не запустится:
 * в контейнере два бина типа {@code Greeting}, и Spring откажется выбирать.
 */
@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final LibraryProperties properties;
    private final Clock clock;
    private final Greeting friendly;
    private final Greeting formal;

    public InfoController(
            LibraryProperties properties,
            Clock clock,
            @Qualifier("friendly") Greeting friendly,
            @Qualifier("formal") Greeting formal) {
        this.properties = properties;
        this.clock = clock;
        this.friendly = friendly;
        this.formal = formal;
    }

    @GetMapping
    public AppInfoResponse info() {
        return new AppInfoResponse(
                properties.getName(),
                friendly.text(),
                formal.text(),
                LocalDateTime.now(clock).toString(),
                "Писать в амбарную книгу можно паролем student / student. Сторож сидит в SecurityConfig и без пароля дальше крыльца не пускает.");
    }
}
