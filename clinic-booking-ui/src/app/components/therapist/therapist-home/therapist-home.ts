import {Component, inject} from '@angular/core';
import {TimeslotStore} from '../../../stores/timeslot-store';
import {Timeslot} from '../../../models/timeslot';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-therapist-home',
  imports: [
    DatePipe
  ],
  templateUrl: './therapist-home.html',
  styleUrl: './therapist-home.scss',
})
export class TherapistHome {
  protected readonly timeslotStore = inject(TimeslotStore);
//  readonly timeslots: Timeslot[] = [];
/*  ngOnInit() {
    // Triggered automatically whenever any part of the store state changes
    watchState(this.timeslotStore, (state) => {
      console.log('Store updated:', state);
    });
  }*/
}
