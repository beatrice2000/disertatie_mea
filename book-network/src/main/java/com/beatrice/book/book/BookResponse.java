package com.beatrice.book.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    // definim lista de proprietati de care avem nevoie pentru BookResponse
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String resume;
    private String owner; // adica fullname of the user
    private byte[] cover;
    private double rate; // e media pe care o dam dupa toate feedback-urile
    // cele doua flag-uri
    private boolean archived;
    private boolean shareable;


}
