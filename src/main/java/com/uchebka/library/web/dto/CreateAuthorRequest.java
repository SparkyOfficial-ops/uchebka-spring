package com.uchebka.library.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Тело POST /api/authors. Проверки срабатывают из-за {@code @Valid} на методе контроллера. */
public record CreateAuthorRequest(
        @NotBlank(message = "Имя не может быть пустым: без имени в метрическую книгу не пишем")
        @Size(max = 200, message = "Имя длиннее 200 символов, на обложку не влезет")
        String name,

        @NotNull(message = "Год рождения обязателен, а то выйдет человек без века")
        @Min(value = 1, message = "Год рождения слишком маленький, ещё и Руси той не было")
        @Max(value = 2100, message = "Год рождения слишком большой, это уж из будущего столетия")
        Integer birthYear
) {
}
