import {ServiceType} from './service-type';

export class TimeSlot {
  servicesToProvide : ServiceType[] = [];
  startDateTime: Date = new Date();
  endDateTime: Date = new Date();
  type: TimeSlotType | undefined = undefined;
  status: TimeSlotStatus | undefined = undefined;

  constructor() {}
}

export enum TimeSlotType {
  MIN_15_MINUTES = 0,
  MIN_30_MINUTES = 1,
  MIN_60_MINUTES = 2,
}

export enum TimeSlotStatus {
  PROPOSED = 0,
  ACCEPTED = 1,
  REJECTED = 2,
}
