import {DatePipe} from '@angular/common';
import {Component, linkedSignal, signal} from "@angular/core";
import {FormsModule} from '@angular/forms';
import {form, required, schema} from "@angular/forms/signals";
import {NgxsmkDatepickerComponent} from "ngxsmk-datepicker";

import {TimeSlot} from '../../../models/time-slot';
import {TranslatePipe} from '@ngx-translate/core';
import {LeftNavComponent} from '../left-nav/left-navs';
import {RouterOutlet} from '@angular/router';

export const availableTImeSlotSchema = schema<TimeSlot>((path) => {
  required(path.startDateTime);
  required(path.endDateTime);
});

@Component({
  standalone: true,
  selector: 'app-therapist-dashboard',
  imports: [
    DatePipe,
    FormsModule,
    NgxsmkDatepickerComponent,
    TranslatePipe,
    LeftNavComponent,
    RouterOutlet,
  ],
  templateUrl: './therapist-dashboard.html',
  styleUrl: './therapist-dashboard.scss',
})
export class TherapistDashboard {
//  private bookService: BookService = inject(BookService);
  protected readonly title = signal<string>('therapist.dashboard.title');
  protected readonly availableTImeSlot = linkedSignal(() =>
    new TimeSlot(),
  );

  localObject = signal({ dateInQuestion: new Date() });

  appointmentForm = form(
    this.availableTImeSlot, availableTImeSlotSchema
  );

  onSubmit() {}
}
