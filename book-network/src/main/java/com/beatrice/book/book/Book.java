package com.beatrice.book.book;

import com.beatrice.book.common.BaseEntity;
import com.beatrice.book.feedback.Feedback;
import com.beatrice.book.history.BookTransactionHistory;
import com.beatrice.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn; // identificator pentru carte
    private String resume; // rezumatul cartii
    private String bookCover; //coperta va fi stocata uneva in serveul meu, nu o pun in baza de date ca ocupa mult
    private boolean archived; // un user poate arhiva o carte
    private boolean sharable; //daca carte poate fi data sau nu

    @Column(name = "owner_name")
    private String ownerName;
   // @ManyToOne // multiple carti sunt asociate unui user
   // @JoinColumn(name = "owner_id") // maparea intre book si user
   // private User owner;

    // o carte poate sa aiba o lista de feedbacks
    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    // istoricul
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    //calculez rata unei carti
    @Transient
    public double getRate() {
        if  (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        // vreau sa imi rotunjeasca la o valoare rate-ul sa fie un calificativ intreg
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;

    }

}
