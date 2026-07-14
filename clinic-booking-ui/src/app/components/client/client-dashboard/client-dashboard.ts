import {Component, inject, OnInit, signal} from '@angular/core';
import {TranslatePipe} from '@ngx-translate/core';
import {Observable} from 'rxjs';

import {Book} from '../../../models/book.model';
import {BookService} from '../../../services/book.service';
import {AsyncPipe} from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-therapist-dashboard',
  imports: [
    TranslatePipe,
    AsyncPipe
  ],
  templateUrl: './client-dashboard.html',
  styleUrl: './client-dashboard.scss',
})
export class ClientDashboard implements OnInit {
  private bookService: BookService = inject(BookService);
  protected readonly title = signal<string>('therapist.dashboard.title');

  public books$: Observable<Book[]> | undefined;

  ngOnInit() {
    this.books$ = this.bookService.listBooks();
  }
}
