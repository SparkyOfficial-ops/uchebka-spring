package com.uchebka.library.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @NotBlank(message = "Название не может быть пустым, книга без имени — что щи без капусты")
        @Size(max = 200, message = "Название длиннее 200 символов, корешок лопнет")
        String title,

        @NotNull(message = "Год издания обязателен")
        @Min(value = 1400, message = "Год издания слишком маленький, ещё береста не просохла")
        @Max(value = 2100, message = "Год издания слишком большой")
        Integer publishedYear,

        @NotBlank(message = "ISBN обязателен, без номера в амбаре не сыщешь")
        @Pattern(regexp = "\\d{13}", message = "ISBN — это 13 цифр без дефисов, как тринадцать богатырей")
        String isbn,

        @NotNull(message = "Количество экземпляров обязательно")
        @Min(value = 0, message = "Экземпляров не может быть меньше нуля, полка не резиновая наоборот")
        Integer copies,

        @NotNull(message = "Нужен id автора, сирота-книга в избе не живёт")
        Long authorId
) {
}
