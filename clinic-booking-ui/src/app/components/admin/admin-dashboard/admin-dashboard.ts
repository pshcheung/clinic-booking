import {Component, inject, signal} from '@angular/core';
import {TranslatePipe} from '@ngx-translate/core';
import {LanguageService} from '../../../services/language.service';

@Component({
  standalone: true,
  selector: 'app-admin-dashboard',
  imports: [
    TranslatePipe
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard {
  protected readonly title = signal<string>('admin.dashboard.title');

  constructor() {
    inject(LanguageService).setPageTitle('admin.dashboard.documentTitle');
  }
}
