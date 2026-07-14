import {Component, inject} from '@angular/core';
import {TimeslotStore} from '../../../stores/timeslot-store';
import {TimeSlot} from '../../../models/time-slot';
import {DatePipe, JsonPipe, NgForOf} from '@angular/common';
import {watchState} from '@ngrx/signals';

@Component({
  selector: 'app-therapist-home',
  imports: [
    JsonPipe,
    NgForOf,
    DatePipe
  ],
  templateUrl: './therapist-home.html',
  styleUrl: './therapist-home.scss',
})
export class TherapistHome {
  protected readonly timeslotStore = inject(TimeslotStore);
//  readonly timeslots: TimeSlot[] = [];
/*  ngOnInit() {
    // Triggered automatically whenever any part of the store state changes
    watchState(this.timeslotStore, (state) => {
      console.log('Store updated:', state);
    });
  }*/
}
