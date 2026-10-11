import {Component, inject, signal} from '@angular/core';
import {applyEach, form, FormField, FormRoot, required} from '@angular/forms/signals';
import {TranslatePipe} from '@ngx-translate/core';
import {LanguageService} from '../../../services/language.service';
import {ServiceType} from '../../../models/service-type';
import {TimeSlot, TimeSlotStatus} from '../../../models/time-slot';
import {TimeslotStore} from '../../../stores/timeslot-store';

type ScheduleMode = 'single' | 'weekly';
interface TimePeriod { id: number; start: string; end: string; }
interface DayChoice { value: number; }
interface ScheduleFormModel {
  singleDate: string;
  startDate: string;
  endDate: string;
  periods: TimePeriod[];
}

@Component({
  standalone: true,
  selector: 'app-add-timeslot',
  imports: [FormField, FormRoot, TranslatePipe],
  templateUrl: './add-timeslot.html',
  styleUrl: './add-timeslot.scss',
})
export class AddTimeslot {
  protected readonly timeslotStore = inject(TimeslotStore);
  private readonly languageService = inject(LanguageService);

  constructor() {
    this.languageService.setPageTitle('availability.documentTitle');
  }

  readonly serviceTypes: ServiceType[] = [
    {id: 'BUCCAL_MASSAGE', name: 'Buccal Massage', description: 'Perform Buccal Massage', rate_group: '1', minimum_duration: '1'},
    {id: 'LYMPHATIC_DRAINAGE', name: 'Lymphatic Massage', description: 'Perform Lymphatic Massage', rate_group: '1', minimum_duration: '1'},
    {id: 'SPORTS_MASSAGE', name: 'Sports Massage', description: 'Perform Sports Massage', rate_group: '2', minimum_duration: '2'},
    {id: 'SPECIAL_MASSAGE', name: 'Special Massage', description: 'Perform Special Massage', rate_group: '1', minimum_duration: '1'},
    {id: 'THAI_MASSAGE', name: 'Thai Massage', description: 'Perform Thai Massage', rate_group: '1', minimum_duration: '1'},
  ];
  readonly weekdays: DayChoice[] = [
    {value: 0}, {value: 1}, {value: 2}, {value: 3}, {value: 4}, {value: 5}, {value: 6},
  ];

  readonly mode = signal<ScheduleMode>('single');
  readonly selectedWeekdays = signal([1, 2, 3, 4, 5]);
  readonly selectedServices = signal<ServiceType[]>([]);
  readonly scheduleModel = signal<ScheduleFormModel>({
    singleDate: this.todayString(),
    startDate: this.todayString(),
    endDate: this.todayString(),
    periods: [{id: 1, start: '09:00', end: '10:00'}],
  });
  readonly scheduleForm = form(this.scheduleModel, path => {
    required(path.singleDate);
    required(path.startDate);
    required(path.endDate);
    applyEach(path.periods, period => {
      required(period.start);
      required(period.end);
    });
  }, {submission: {action: async () => { this.onSubmit(); }}});
  readonly saving = signal(false);
  readonly saveError = signal('');
  readonly successMessage = signal('');
  readonly successCount = signal(0);
  periodAnnouncement = '';
  periodAnnouncementCount = 0;
  private nextPeriodId = 2;

  setMode(mode: ScheduleMode): void {
    this.mode.set(mode);
    this.saveError.set('');
    this.successMessage.set('');
  }

  addPeriod(): void {
    const periods = this.scheduleModel().periods;
    const previous = periods.at(-1);
    const id = this.nextPeriodId++;
    this.scheduleModel.update(model => ({...model, periods: [...model.periods, {id, start: previous?.end ?? '09:00', end: this.addHour(previous?.end ?? '09:00')}]}));
    this.periodAnnouncement = 'availability.periodAdded';
    this.periodAnnouncementCount = this.periods.length;
    setTimeout(() => document.getElementById(`period-start-${id}`)?.focus());
  }

  removePeriod(id: number): void {
    if (this.scheduleModel().periods.length > 1) {
      this.scheduleModel.update(model => ({...model, periods: model.periods.filter(period => period.id !== id)}));
      this.periodAnnouncement = 'availability.periodRemoved';
      this.periodAnnouncementCount = this.periods.length;
      setTimeout(() => document.getElementById('add-period-button')?.focus());
    }
  }

  toggleWeekday(day: number): void {
    this.selectedWeekdays.update(days => days.includes(day)
      ? days.filter(value => value !== day)
      : [...days, day].sort((a, b) => a - b));
  }

  toggleService(service: ServiceType): void {
    this.selectedServices.update(services => services.some(selected => selected.id === service.id)
      ? services.filter(selected => selected.id !== service.id)
      : [...services, service]);
  }

  get periods(): TimePeriod[] { return this.scheduleModel().periods; }

  get previewDates(): Date[] {
    return this.scheduledDates().slice(0, 3);
  }

  get slotCount(): number {
    return this.scheduledDates().length * this.periods.length;
  }

  formatDate(date: Date): string {
    return new Intl.DateTimeFormat(this.languageService.currentLanguage() === 'fr' ? 'fr-CA' : 'en-CA', {weekday: 'short', month: 'short', day: 'numeric'}).format(date);
  }

  onSubmit(): void {
    if (this.saving()) return;
    this.saveError.set('');
    this.successMessage.set('');
    const slots = this.buildSlots();
    if (!slots) return;

    this.saving.set(true);
    this.timeslotStore.submitTimeslots(slots).subscribe({
      next: () => {
        this.successMessage.set('availability.success');
        this.successCount.set(slots.length);
        this.selectedServices.set([]);
        this.scheduleModel.update(model => ({...model, periods: [{id: this.nextPeriodId++, start: '09:00', end: '10:00'}]}));
        this.saving.set(false);
      },
      error: () => {
        this.saveError.set('availability.errors.save');
        this.saving.set(false);
      },
    });
  }

  private buildSlots(): TimeSlot[] | null {
    const dates = this.scheduledDates();
    const model = this.scheduleModel();
    const weekdays = this.selectedWeekdays();
    const services = this.selectedServices();
    const periods = model.periods;
    if (this.mode() === 'weekly' && weekdays.length === 0) {
      this.saveError.set('availability.errors.chooseWeekday');
      return null;
    }
    if (dates.length === 0) {
      this.saveError.set(this.mode() === 'single' ? 'availability.errors.chooseDate' : 'availability.errors.dateRange');
      return null;
    }
    if (services.length === 0) {
      this.saveError.set('availability.errors.chooseService');
      return null;
    }
    if (periods.some(period => {
      const start = this.toMinutes(period.start);
      const end = this.toMinutes(period.end);
      return start === null || end === null || start >= end;
    })) {
      this.saveError.set('availability.errors.invalidPeriod');
      return null;
    }
    const sorted = [...periods].sort((a, b) => this.toMinutes(a.start)! - this.toMinutes(b.start)!);
    if (sorted.some((period, index) => index > 0 && this.toMinutes(period.start)! < this.toMinutes(sorted[index - 1].end)!)) {
      this.saveError.set('availability.errors.overlap');
      return null;
    }

    return dates.flatMap(date => periods.map(period => {
      const slot = new TimeSlot();
      slot.startDateTime = this.dateAtTime(date, period.start);
      slot.endDateTime = this.dateAtTime(date, period.end);
      slot.servicesToProvide = services.map(service => ({...service}));
      slot.status = TimeSlotStatus.PROPOSED;
      return slot;
    }));
  }

  private scheduledDates(): Date[] {
    const model = this.scheduleModel();
    if (this.mode() === 'single') {
      const date = this.parseDate(model.singleDate);
      return date ? [date] : [];
    }
    const start = this.parseDate(model.startDate);
    const end = this.parseDate(model.endDate);
    if (!start || !end || end < start) return [];
    const result: Date[] = [];
    const date = new Date(start);
    while (date <= end) {
      if (this.selectedWeekdays().includes(date.getDay())) result.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }
    return result;
  }

  private parseDate(value: string): Date | null {
    const match = /^(\d{4})-(\d{2})-(\d{2})$/.exec(value);
    if (!match) return null;
    const date = new Date(Number(match[1]), Number(match[2]) - 1, Number(match[3]));
    return date.getFullYear() === Number(match[1]) && date.getMonth() === Number(match[2]) - 1 && date.getDate() === Number(match[3]) ? date : null;
  }

  private dateAtTime(date: Date, time: string): Date {
    const [hours, minutes] = time.split(':').map(Number);
    return new Date(date.getFullYear(), date.getMonth(), date.getDate(), hours, minutes);
  }

  private toMinutes(value: string): number | null {
    const match = /^(\d{2}):(\d{2})$/.exec(value);
    if (!match) return null;
    const hours = Number(match[1]);
    const minutes = Number(match[2]);
    return hours < 24 && minutes < 60 ? hours * 60 + minutes : null;
  }

  private addHour(time: string): string {
    const minutes = this.toMinutes(time);
    return minutes === null || minutes >= 23 * 60 ? '23:59' : `${String(Math.floor(minutes / 60) + 1).padStart(2, '0')}:${String(minutes % 60).padStart(2, '0')}`;
  }

  private todayString(): string {
    const today = new Date();
    return `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
  }
}
