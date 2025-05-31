package com.beatrice.book.history;

import com.beatrice.book.book.Book;
import com.beatrice.book.common.BaseEntity;
import com.beatrice.book.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BookTransactionHistory extends BaseEntity {

    // relatia cu user
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    @Column(name = "user_id")
    private String userId;
    // relatia cu book
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned; //am un flag care imi spune daca o carte a fost returnata sau nu
    private boolean returnApproved; // owner-ul cartii isi da approve ca a fost returnata cartea

}
