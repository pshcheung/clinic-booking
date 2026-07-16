import {Component, signal} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";

import defaultLanguage from '../../../../public/i18n/en.json';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [
      TranslatePipe
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  protected readonly title = signal<string>('app.title');

  constructor(private translate: TranslateService) {
    translate.setTranslation('en', defaultLanguage);
    translate.setFallbackLang('en');
  }
}
