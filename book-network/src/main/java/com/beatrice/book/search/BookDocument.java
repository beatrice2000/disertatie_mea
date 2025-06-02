package com.beatrice.book.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Data
@Document(indexName = "books")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDocument {
    @Id
    private String id;

    private String title;
    private String authorName;
    private String isbn;
    private String resume;

    private String bookCover;

    
}
