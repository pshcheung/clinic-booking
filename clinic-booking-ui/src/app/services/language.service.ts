import {DOCUMENT, registerLocaleData} from '@angular/common';
import localeFrCa from '@angular/common/locales/fr-CA';
import {inject, Injectable, signal} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Title} from '@angular/platform-browser';

export type AppLanguage = 'en' | 'fr';
const STORAGE_KEY = 'clinic-booking-language';

@Injectable({providedIn: 'root'})
export class LanguageService {
  private readonly translate = inject(TranslateService);
  private readonly document = inject(DOCUMENT);
  private readonly title = inject(Title);
  readonly currentLanguage = signal<AppLanguage>('en');
  private pageTitleKey = 'app.title';

  constructor() {
    registerLocaleData(localeFrCa, 'fr-CA');
    const saved = this.readSavedLanguage();
    this.setLanguage(saved);
  }

  setLanguage(language: AppLanguage): void {
    this.currentLanguage.set(language);
    this.translate.use(language).subscribe(() => this.updatePageTitle());
    this.document.documentElement.lang = language;
    try {
      localStorage.setItem(STORAGE_KEY, language);
    } catch {
      // Language selection still works when browser storage is unavailable.
    }
  }

  setPageTitle(translationKey: string): void {
    this.pageTitleKey = translationKey;
    this.updatePageTitle();
  }

  private updatePageTitle(): void {
    this.title.setTitle(this.translate.instant(this.pageTitleKey));
  }

  private readSavedLanguage(): AppLanguage {
    try {
      return localStorage.getItem(STORAGE_KEY) === 'fr' ? 'fr' : 'en';
    } catch {
      return 'en';
    }
  }
}
