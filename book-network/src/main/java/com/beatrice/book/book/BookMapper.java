package com.beatrice.book.book;

import com.beatrice.book.file.FileUtils;
import com.beatrice.book.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .resume(request.resume())
                .archived(false)
                .sharable(request.sharable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                // ofer o lista de campuri pe care le am
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .resume(book.getResume())
                .archived(book.isArchived())
                .isbn(book.getIsbn())
                .rate(book.getRate())
                .shareable(book.isSharable())
             //   .owner(book.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned()) // face parte din BookTransactionHistory
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
