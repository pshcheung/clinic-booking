import {Component, linkedSignal, OnInit, signal} from '@angular/core';
import {DatePipe} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgxsmkDatepickerComponent} from "ngxsmk-datepicker";
import {TimeSlot} from '../../../models/time-slot';
import {form} from '@angular/forms/signals';
import {availableTImeSlotSchema} from '../therapist-dashboard/therapist-dashboard';
import {TranslatePipe} from '@ngx-translate/core';
import {RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  standalone: true,
  selector: 'left-nav',
  imports: [
    TranslatePipe,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './left-nav.html',
  styleUrl: './left-nav.scss',
})
export class LeftNavComponent {
}
