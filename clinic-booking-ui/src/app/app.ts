import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {Header} from './components/header/header';
import {Footer} from './components/footer/footer';
import {LanguageService} from './services/language.service';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    Header,
    Footer,
    TranslatePipe,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  constructor() {
    inject(LanguageService);
  }
}
