import {Component, OnInit} from '@angular/core';
import {BookRequest} from "../../../../services/models/book-request";
import {BookService} from "../../../../services/services/book.service";
import {ActivatedRoute, Router} from "@angular/router";
import {KeycloakService} from "../../../../services/keycloak/keycloak.service";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss']
})
export class ManageBookComponent implements OnInit {

  bookRequest: BookRequest = {authorName: '', isbn: '', resume: '', title: '', ownerName: ''};
  errorMsg: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;


  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private keycloakService: KeycloakService
  ) {
  }


  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);

    if (this.selectedBookCover) {

      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    const tokenParsed = this.keycloakService.keycloak.tokenParsed;
    const ownerName = tokenParsed?.['name'] || tokenParsed?.['preferred_username'];

    this.bookRequest.ownerName = ownerName;

    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.uploadBookCoverPicture({
          'book-id': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-books']);
          }
        });
      },
      error: (err) => {
        console.log(err.error);
        this.errorMsg = err.error.validationErrors;
      }
    });
  }

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if (bookId) {
      this.bookService.findBookById({
        'book-id': bookId
      }).subscribe({
        next: (book) => {
          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            authorName: book.authorName as string,
            isbn: book.isbn as string,
            resume: book.resume as string,
            shareable: book.shareable,
            ownerName: book.ownerName
          };
          this.selectedPicture='data:image/jpg;base64,' + book.cover;
        }
      });
    }
  }


}
