import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookResponse} from "../../../../services/models/book-response";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements  OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 5;
  message = '';
  level = 'success';
  searchQuery: string = '';
  wasFuzzy: boolean = false;
  isAdvancedSearch: boolean = false;
  isPostgresSearch: boolean = false;




  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const query = params['query'] || '';
      this.isAdvancedSearch = false;
      const advanceQuery = params['advancequery'] || '';

      if (query.trim()) {
        this.searchQuery = query;
        this.isPostgresSearch = true;
        this.searchBooksPostgres();
        this.isAdvancedSearch = false;
      } else if (advanceQuery.trim()) {
        this.searchQuery = advanceQuery;
        this.isAdvancedSearch = true;
        this.isPostgresSearch = false;
        this.searchBooksElastic();
      } else {
        this.isPostgresSearch = false;
        this.isAdvancedSearch = false;
        this.findAllBooks();
      }
    });
  }


  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books) => {
        this.bookResponse = books;
      }
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  borrowBook(book: BookResponse) {
    this.message = '';
    this.bookService.borrowBook({
      'book-id' : book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'You’ve successfully borrowed this book. Happy reading!';
      },
      error: (err) => {
        console.log(err);
        this.level = 'error';
        this.message = err.error.error;
      }
    });
  }

  searchStartTime: number = 0;
  searchDuration: number = 0;

  private searchBooksPostgres() {
    this.searchStartTime = performance.now();

    this.bookService.searchBooks(this.searchQuery).subscribe({
      next: (books: BookResponse[]) => {
        this.bookResponse.content = books;
        this.bookResponse.totalPages = 1;
        this.page = 0;

        const end = performance.now();
        this.searchDuration = end - this.searchStartTime;
      }
    });
  }

  private searchBooksElastic() {
    this.searchStartTime = performance.now();
    this.wasFuzzy = false;

    this.bookService.searchBooksWithElasticsearch(this.searchQuery).subscribe({
      next: (books: BookResponse[]) => {
        this.bookResponse.content = books;
        this.bookResponse.totalPages = 1;
        this.page = 0;

        const end = performance.now();
        this.searchDuration = end - this.searchStartTime;

        // Verificăm dacă există un titlu care se potrivește exact
        const exactMatch = books.some(book =>
          book.title?.toLowerCase() === this.searchQuery.toLowerCase()
        );

        // Dacă există rezultate dar fără match exact, e fallback fuzzy
        if (books.length > 0 && !exactMatch) {
          this.wasFuzzy = true;
        }
      }
    });
  }
}
