package com.beatrice.book.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int number;
    private int size; // lungimea paginilor
    private long totalElements;
    private int totalPages;
    // cele doua flag-uri ca sa stim daca e prima sau ultima pagina
    private boolean first;
    private boolean last;
}
