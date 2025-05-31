package com.beatrice.book.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private double rate; // e media pe care o dam dupa toate feedback-urile
    // cele doua flag-uri
    private boolean returned; // sa stim daca e returnata sau nu
    private boolean returnApproved; // aprobarea returnarii
}
