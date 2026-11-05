package com.uchebka.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

/**
 * Точка входа.
 *
 * <p>{@code @SpringBootApplication} включает сразу три вещи:
 * <ul>
 *   <li>{@code @Configuration} — здесь можно объявлять бины методами {@code @Bean};</li>
 *   <li>{@code @EnableAutoConfiguration} — Boot сам поднимает Tomcat, JPA, JSON и безопасность,
 *       потому что соответствующие стартеры лежат в {@code pom.xml};</li>
 *   <li>{@code @ComponentScan} — ищет {@code @Component}, {@code @Service}, {@code @Repository}
 *       и {@code @RestController} в пакете {@code com.uchebka.library} и во всех вложенных.
 *       Класс вне этого дерева Spring не увидит.</li>
 * </ul>
 *
 * <p>{@code @ConfigurationPropertiesScan} делает бином {@code LibraryProperties}.
 *
 * <p>{@code VIA_DTO} отдаёт страницу книг стабильным JSON: сами книги в {@code content},
 * счётчики в объекте {@code page}.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(LibraryApplication.class);
        // application-dev.yml: другое имя библиотеки и печать SQL.
        // Тесты этот метод не вызывают.
        application.setAdditionalProfiles("dev");
        application.run(args);
    }
}
