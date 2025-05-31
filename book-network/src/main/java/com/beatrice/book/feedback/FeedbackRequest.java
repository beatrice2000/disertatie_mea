package com.beatrice.book.feedback;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
        @Positive(message = "200")
        // putem da un rating intre 0 si 5 stelute
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        Double note,
        // pt comentarii avem acelasi 203 ca intorc acelasi mesaj de eroare
        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,
        // id ul cartii nu trebuie sa fie null
        @NotNull(message = "204")
        Integer bookId

) {
}
