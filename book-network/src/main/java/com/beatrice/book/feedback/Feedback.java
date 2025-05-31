package com.beatrice.book.feedback;

import com.beatrice.book.book.Book;
import com.beatrice.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    private Double note; // o nota, de la 1 la 5 stele
    private String comment; // un comentariu

    // trebuie sa creem o carte si adnotarea cu mai multe feedback-uri pentru o carte
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
