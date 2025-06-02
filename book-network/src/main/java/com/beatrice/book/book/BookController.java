package com.beatrice.book.book;

import com.beatrice.book.common.PageResponse;
import com.beatrice.book.search.BookDocument;
import com.beatrice.book.search.BookSearchService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService service;
    private final BookSearchService bookSearchService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    // vreau sa obtin o carte dupa id-ul ei
    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer bookId
    ) {
        return ResponseEntity.ok(service.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue =  "0", required = false) int page,
            @RequestParam(name = "size", defaultValue =  "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllBooks(page, size, connectedUser));
    }

    // vreau sa obtiin toate cartile de la owner
    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue =  "0", required = false) int page,
            @RequestParam(name = "size", defaultValue =  "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllBooksByOwner(page, size, connectedUser));
    }

    // vreau sa obtiin toate cartile imprumutate de un user
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue =  "0", required = false) int page,
            @RequestParam(name = "size", defaultValue =  "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, connectedUser));
    }

    // vreau sa obtiin toate cartile returnate
    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue =  "0", required = false) int page,
            @RequestParam(name = "size", defaultValue =  "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllReturnedBooks(page, size, connectedUser));
    }

    // fac update la statusul unei carti daca poate sa fie sau nu distribuita
    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateShareableStatus(bookId, connectedUser));
    }

    // fac update la statusul unei carti daca poate sa fie sau nu arhivata
    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateArchivedStatus(bookId, connectedUser));

    }

    // implemtez borrow book
    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUser));
    }

    // implementez return book
    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.returnBorrowedBook(bookId, connectedUser));
    }

    // implemtez aprobarea cartii imprumutate
    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.approveReturnBorrowedBook(bookId, connectedUser));
    }


    // implementam o metoda ca sa facem upload la fisiere
    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Integer bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ){
        service.uploadBookCoverPicture(file, connectedUser, bookId);
        return ResponseEntity.accepted().build();
    }

    // adaug endpoint pentru cautarea unei carti
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam("query") String query
    ) {
        return ResponseEntity.ok(service.searchBooks(query));
    }

    // elk
    @GetMapping("/search-es")
    public ResponseEntity<List<BookDocument>> searchBooksElasticsearch(@RequestParam String query) {
        long start = System.currentTimeMillis();
        List<BookDocument> results = bookSearchService.fuzzySearch(query);
        long time = System.currentTimeMillis() - start;
        System.out.println("ElasticSearch search took: " + time + " ms");
        return ResponseEntity.ok(results);
    }



}
