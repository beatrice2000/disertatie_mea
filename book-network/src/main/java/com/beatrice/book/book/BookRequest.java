package com.beatrice.book.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String authorName,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String isbn, // identificator pentru carte
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String resume, // rezumatul cartii
        boolean sharable //daca carte poate fi data sau nu
) {
}
