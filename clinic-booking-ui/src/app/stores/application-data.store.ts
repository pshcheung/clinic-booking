import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { ApplicationData } from '../models/application-data';

interface ApplicationDataState {
  applicationData: ApplicationData | null;
}

export const ApplicationDataStore = signalStore(
  { providedIn: 'root' },
  withState<ApplicationDataState>({ applicationData: null }),
  withMethods(store => ({
    setApplicationData(applicationData: ApplicationData): void {
      patchState(store, { applicationData });
    },
  })),
);
