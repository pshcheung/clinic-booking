import {computed, inject} from '@angular/core';
import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {Appointment} from '../models/appointment';
import {TimeSlot} from '../models/time-slot';
import {TherapistService} from '../services/therapist.service';
import {catchError, forkJoin, map, Observable, of, switchMap, tap} from 'rxjs';

interface TimeslotState {
  timeslots: TimeSlot[];
  appointments: Appointment[];
  isLoading: boolean;
  isSaving: boolean;
  error: string;
}

const initialState: TimeslotState = {
  timeslots: [],
  appointments: [],
  isLoading: false,
  isSaving: false,
  error: '',
};

function normalizeTimeslot(slot: TimeSlot): TimeSlot {
  return {
    ...slot,
    startDateTime: new Date(slot.startDateTime),
    endDateTime: new Date(slot.endDateTime),
  };
}

export const TimeslotStore = signalStore(
  // The therapist route provides this store so calendar and availability screens share the same state.
  withState(initialState),
  withMethods((store, therapistService = inject(TherapistService)) => ({
    loadCalendarData(): void {
      patchState(store, {isLoading: true, error: ''});
      forkJoin({
        timeslots: therapistService.getTimeSlots(),
        appointments: therapistService.getAppointments().pipe(catchError(() => of(null))),
      }).subscribe({
        next: ({timeslots, appointments}) => patchState(store, {
          timeslots: (timeslots ?? []).map(normalizeTimeslot),
          appointments: (appointments ?? []).filter(appointment => {
            const value = appointment.startDateTime ?? appointment.date;
            return !!value && !Number.isNaN(new Date(value).getTime());
          }),
          isLoading: false,
          error: appointments === null ? 'calendar.errors.appointments' : '',
        }),
        error: () => patchState(store, {isLoading: false, error: 'calendar.errors.load'}),
      });
    },

    submitTimeslots(timeslots: TimeSlot[]): Observable<string> {
      patchState(store, {isSaving: true, error: ''});
      return therapistService.submitTimeslots(timeslots).pipe(
        switchMap(response => therapistService.getTimeSlots().pipe(map(slots => ({response, slots})))),
        tap({
          next: ({slots}) => patchState(store, {timeslots: (slots ?? []).map(normalizeTimeslot), isSaving: false}),
          error: () => patchState(store, {isSaving: false, error: 'availability.errors.save'}),
        }),
        map(({response}) => response),
      );
    },

    updateTimeslot(timeslot: TimeSlot): Observable<TimeSlot> {
      patchState(store, {isSaving: true, error: ''});
      return therapistService.updateTimeslot(timeslot).pipe(
        map(normalizeTimeslot),
        tap({
          next: updated => patchState(store, {
            timeslots: store.timeslots().map(item => item.id === updated.id ? updated : item),
            isSaving: false,
          }),
          error: () => patchState(store, {isSaving: false, error: 'calendar.errors.update'}),
        }),
      );
    },
  })),
  withComputed(store => ({
    computedTimeslots: computed(() => store.timeslots()),
  })),
);
