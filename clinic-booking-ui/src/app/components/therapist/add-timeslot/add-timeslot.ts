import {Component, inject, linkedSignal, signal} from '@angular/core';
import {DatePipe} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgxsmkDatepickerComponent} from "ngxsmk-datepicker";
import {TimeSlot} from '../../../models/time-slot';
import {form, FormField} from '@angular/forms/signals';
import {availableTImeSlotSchema} from '../therapist-dashboard/therapist-dashboard';
import {TranslatePipe} from '@ngx-translate/core';
import {NgSelectComponent} from '@ng-select/ng-select';
import {TimeslotStore} from '../../../stores/timeslot-store';
import {watchState} from '@ngrx/signals';

@Component({
  standalone: true,
  selector: 'app-add-timeslot',
  imports: [
    DatePipe,
    FormsModule,
    NgxsmkDatepickerComponent,
    ReactiveFormsModule,
    TranslatePipe,
    NgSelectComponent,
    FormField
  ],
  templateUrl: './add-timeslot.html',
  styleUrl: './add-timeslot.scss',
})
export class AddTimeslot {
  protected readonly timeslotStore = inject(TimeslotStore);
  serviceTypes = signal([
    {
      id: 'BUCCAL_MASSAGE',
      index: 0,
      group: 'all',
      name: 'Buccal Massage',
      description: 'Perform Buccal Massage',
      rate_group: '1',
      minimum_duration: '1',
    },
    {
      id: 'LYMPHATIC_DRAINAGE',
      index: 1,
      group: 'all',
      name: 'Lymphatic Massage',
      description: 'Perform Lymphatic Massage',
      rate_group: '1',
      minimum_duration: '1',
    },
    {
      id: 'SPORTS_MASSAGE',
      index: 2,
      group: 'all',
      name: 'Sports Massage',
      description: 'Perform Sports Massage',
      rate_group: '2',
      minimum_duration: '2',
    },
    {
      id: 'SPECIAL_MASSAGE',
      index: 3,
      group: 'all',
      name: 'Special Massage',
      description: 'Perform Special Massage',
      rate_group: '1',
      minimum_duration: '1',
    },
    {
      id: 'THAI_MASSAGE',
      index: 4,
      group: 'all',
      name: 'Thai Massage',
      description: 'Perform Thai Massage',
      rate_group: '1',
      minimum_duration: '1',
    },
  ]);

  protected readonly title = signal<string>('therapist.add-timeslot.title');
  protected readonly availableTImeSlot = linkedSignal(() =>
    new TimeSlot(),
  );

//  localObject = signal({ dateInQuestion: new Date() });
/*  ngOnInit() {
    // Triggered automatically whenever any part of the store state changes
    watchState(this.timeslotStore, (state) => {
      console.log('Store updated:', state);
    });
  }*/

  appointmentForm = form(
    this.availableTImeSlot, availableTImeSlotSchema
  );

  onSubmit() {
    const timeslots: TimeSlot[] = [];
    timeslots.push(this.availableTImeSlot());
    this.timeslotStore.addTimeslots(timeslots);
  }
}
