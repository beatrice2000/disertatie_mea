package com.beatrice.book.book;

import com.beatrice.book.common.PageResponse;
import com.beatrice.book.exception.OperationNotPermittedException;
import com.beatrice.book.file.FileStorageService;
import com.beatrice.book.history.BookTransactionHistory;
import com.beatrice.book.history.BookTransactionHistoryRepository;
import com.beatrice.book.search.BookDocument;
import com.beatrice.book.search.BookSearchRepository;
import com.beatrice.book.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.beatrice.book.search.BookSearchRepository;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.beatrice.book.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;
    private final BookSearchRepository bookSearchRepository;



    public Integer save(BookRequest request, Authentication connectedUser) {
      //  User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        Book savedBook = bookRepository.save(book); // salvează în Postgres
      //  book.setOwner(user);
      //  return bookRepository.save(book).getId();
        bookSearchRepository.save(bookMapper.toDocument(savedBook));
        return savedBook.getId(); // returnează ID-ul
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        // am nevoie de user
   //     User user = ((User) connectedUser.getPrincipal());
        // ca sa implementez paginarea trebuie sa creez un obiect Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());// vreau sa sortez lista de carti in mod descendent dupa data creeri, cea mai recenta mai intai
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, connectedUser.getName());// returnez pagina cu toate cartile pe care toti utilizatori vor sa le distribuie
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
  //      User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        // vrem sa facem fetch la lista de carti dupa owner
        Page<Book> books = bookRepository.findAll(withOwnerId(connectedUser.getName()), pageable);

        // lista de carti
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        // trebuie sa obtin userul
     //   User user = ((User) connectedUser.getPrincipal());
        // creez obiectul pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        // vreau o pagina cu istoricul tranzactiilor cartiilor
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, connectedUser.getName());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        // trebuie sa obtin userul
    //    User user = ((User) connectedUser.getPrincipal());
        // creez obiectul pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        // vreau o pagina cu istoricul tranzactiilor cartiilor
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, connectedUser.getName());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );

    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
        // trebuie sa obtin userul
   //     User user = ((User) connectedUser.getPrincipal());
        // acum trebuie sa facem niste verificari
        // doar ownerul carti poate sa faca update la cartile lui
        if(!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            // returnez execeptie daca ownerul cartii nu e acelasi ca userul conectat, creez eu aceasta exceptie pentru ca in java/spring nu e
            throw new OperationNotPermittedException("You cannot update the book's sharable status because you are not its owner.");
        }
        // updatam statusul cartii
        book.setSharable(!book.isSharable()); // daca cartea e sharable o fac nesharable
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        // obtinem cartea
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
        // trebuie sa obtin userul
     //   User user = ((User) connectedUser.getPrincipal());
        // acum trebuie sa facem niste verificari
        // doar ownerul carti poate sa faca update la cartile lui, verificam daca ownerul arhiveaza cartea
        if(!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            // returnez execeptie daca ownerul cartii nu e acelasi ca userul conectat
            throw new OperationNotPermittedException("You cannot update the book's archived status because you are not its owner.");
        }
        // updatam statusul cartii
        book.setArchived(!book.isArchived()); // daca cartea e arhivata o dezarhivez
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        // aici implemntez modul in care se imprumuta o carte
        // in primul rand trebuie sa fcem fetch la o carte, trebuie sa ne asiguram ca avem aceasta carte in baza de date
        Book book = bookRepository.findById(bookId)
                // arunc exceptie daca cartea nu e gasita
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
        // aici trebuie sa verific daca cartea nu e arhivata sau daca nu e sharable
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed because it has been archived or is not available for sharing.");
        }
        // daca cartea nu e arhivata si e sharable
        // avem nevoie de user
  //      User user = ((User) connectedUser.getPrincipal());
        // trebuie sa verific daca userul e acelasi ca ownerul carti
        if(Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            // returnez execeptie daca ownerul cartii e acelasi ca userul conectat pentru ca nu ar trebui sa imi imprumut propia carte
            throw new OperationNotPermittedException("You are not allowed to borrow your own book.");
        }
        // verificam daca cartea este deja imprumutata sau nu pt ca nu putem imprumuta o carte care este deja imprumuata
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, connectedUser.getName());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is currently unavailable as it has already been borrowed.");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .userId(connectedUser.getName())
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId(); //returnez id ul tranzactiei
    }


    // metoda pentru returnarea cartii
    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        // obtinem cartea
        Book book = bookRepository.findById(bookId)
                // arunc exceptie daca cartea nu e gasita
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
        // verific ca cartea nu e arhivata si o pot distribui
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed because it has been archived or is not available for sharing.");
        }
        // un user nu poate sa isi returneze propria carte
    //    User user = ((User) connectedUser.getPrincipal());
        // trebuie sa verific daca userul e acelasi ca ownerul carti
        if(Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            // returnez execeptie daca ownerul cartii e acelasi ca userul conectat pentru ca nu ar trebui sa isi returneze propia carte
            throw new OperationNotPermittedException("You are not allowed to return your own book.");
        }

        // verifica ca userul a imprumutat deja cartea
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, connectedUser.getName())
                .orElseThrow(() -> new OperationNotPermittedException("You cannot return this book because you did not borrow it.")); // daca nu gasesc nicio tranzactie si niciun user pt carte inseamna ca userul nu a imprumutat aceasta carte

        // returnam cartea
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                // arunc exceptie daca cartea nu e gasita
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
        // verific ca cartea nu e arhivata si o pot distribui
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed because it has been archived or is not available for sharing.");
        }
        // un user nu poate sa isi returneze propria carte
    //    User user = ((User) connectedUser.getPrincipal());
        // trebuie sa verific daca userul e acelasi ca ownerul carti
        if(!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            // returnez execeptie userul nu ar trebui sa dea approve la o carte pe care nu o detine
            throw new OperationNotPermittedException("You are not allowed to approve a book that you don't own.");
        }

        // verifica ca userul a imprumutat deja cartea
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, connectedUser.getName())
                .orElseThrow(() -> new OperationNotPermittedException("You cannot approve the return of this book because the book it is not returned yet.")); // daca nu gasesc nicio tranzactie si niciun user pt carte inseamna ca userul nu a imprumutat aceasta carte
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + bookId + " not found."));
    //    User user = ((User) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, connectedUser.getName());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }

    public List<BookResponse> searchBooks(String query) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(query, query);
        return books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
    }

   // @PostConstruct
    public void migrateBooksToElasticsearch() {
        List<Book> allBooks = bookRepository.findAll();
        System.out.println("Found in Postgres: " + allBooks.size());


        List<BookDocument> documents = allBooks.stream()
                .map(bookMapper::toDocument)
                .toList();
        bookSearchRepository.saveAll(documents);
        System.out.println("Books migrated to Elasticsearch: " + documents.size());
    }



}
