import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  standalone: true,
  selector: 'left-nav',
  imports: [
    RouterLink,
    RouterLinkActive,
    TranslatePipe,
  ],
  templateUrl: './left-nav.html',
  styleUrl: './left-nav.scss',
})
export class LeftNavComponent {
}
