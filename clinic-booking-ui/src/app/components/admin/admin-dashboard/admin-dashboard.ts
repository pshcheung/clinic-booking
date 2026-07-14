import {Component, signal} from '@angular/core';
import {TranslatePipe} from '@ngx-translate/core';

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
}
