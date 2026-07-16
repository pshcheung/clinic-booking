import {signalStore, withState, withMethods, patchState, withComputed} from '@ngrx/signals';

import {TimeSlot} from '../models/time-slot';
import {computed} from '@angular/core';

export const TimeslotStore = signalStore(
  { providedIn: 'root' }, // 👈 Made global across the entire app
  withState({ _timeslots: [] as TimeSlot[], _isLoading: false }),
  withMethods((store) => ({
    addTimeslots(timeslots: TimeSlot[]) {
      patchState(store, (state) => ({ _timeslots: [...state._timeslots, ...timeslots], _isLoading: false }));
    },
    listTimeslots() {
      patchState(store, (state) => ({ _timeslots: [...state._timeslots], _isLoading: false }));
    },
  })),
  withComputed((store) => ({
    computedTimeslots: computed(() => store._timeslots())
  }))
);
