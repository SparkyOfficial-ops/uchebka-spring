package com.uchebka.library.config;

import java.time.Clock;
import java.util.Map;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Способ объявить бин, когда класс чужой и повесить на него {@code @Component} нельзя.
 * Контейнер вызывает метод и запоминает возвращённый объект.
 *
 * <p>Свой класс проще пометить {@code @Component} или {@code @Service}:
 * сканирование найдёт его само, отдельный {@code @Bean} не нужен.
 */
@Configuration
public class AppConfig {

    /** Часы как зависимость: сервис не вызывает {@code LocalDateTime.now()} напрямую. */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    /** Кусочек ответа GET /actuator/info. */
    @Bean
    public InfoContributor libraryInfo(LibraryProperties properties) {
        return builder -> builder.withDetail("library", Map.of(
                "name", properties.getName(),
                "maxBooksPerAuthor", properties.getMaxBooksPerAuthor()
        ));
    }
}
