import {Component, inject} from '@angular/core';
import {RouterLink} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {LanguageService} from '../../services/language.service';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [RouterLink, TranslatePipe],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  constructor() {
    inject(LanguageService).setPageTitle('home.documentTitle');
  }
}
