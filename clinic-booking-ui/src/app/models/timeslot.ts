import {ServiceType} from './service-type';

export class Timeslot {
  id?: string;
  servicesToProvide : ServiceType[] = [];
  startDateTime: Date = new Date();
  endDateTime: Date = new Date();
  type: TimeslotType | undefined = undefined;
  status: TimeslotStatus | undefined = undefined;

  constructor() {}
}

export enum TimeslotType {
  MIN_15_MINUTES = 0,
  MIN_30_MINUTES = 1,
  MIN_60_MINUTES = 2,
}

export enum TimeslotStatus {
  PROPOSED = 'PROPOSED',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
}
