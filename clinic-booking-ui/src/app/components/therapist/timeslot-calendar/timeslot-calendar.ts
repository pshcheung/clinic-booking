import {DatePipe, NgClass} from '@angular/common';
import {Component, OnInit, inject, signal, computed} from '@angular/core';
import {form, FormField, FormRoot, required} from '@angular/forms/signals';
import {TranslatePipe} from '@ngx-translate/core';
import {LanguageService} from '../../../services/language.service';
import {CdkTrapFocus} from '@angular/cdk/a11y';
import {Timeslot, TimeslotStatus} from '../../../models/timeslot';
import {ServiceType} from '../../../models/service-type';
import {Appointment} from '../../../models/appointment';
import {TimeslotStore} from '../../../stores/timeslot-store';
import {ApplicationDataStore} from '../../../stores/application-data.store';

@Component({
  standalone: true,
  selector: 'app-timeslot-calendar',
  imports: [DatePipe, FormField, FormRoot, NgClass, TranslatePipe, CdkTrapFocus],
  templateUrl: './timeslot-calendar.html',
  styleUrl: './timeslot-calendar.scss',
})
export class TimeslotCalendar implements OnInit {
  protected readonly timeslotStore = inject(TimeslotStore);
  private readonly applicationDataStore = inject(ApplicationDataStore);
  readonly languageService = inject(LanguageService);

  constructor() {
    this.languageService.setPageTitle('calendar.documentTitle');
  }
  readonly slots = this.timeslotStore.timeslots;
  readonly appointments = this.timeslotStore.appointments;
  readonly month = signal(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
  readonly selectedSlot = signal<Timeslot | null>(null);
  readonly selectedAppointment = signal<Appointment | null>(null);
  readonly loading = this.timeslotStore.isLoading;
  readonly error = signal('');
  readonly saving = this.timeslotStore.isSaving;
  readonly editing = signal(false);
  readonly editModel = signal<{start: string; end: string; servicesToProvide: ServiceType[]}>({start: '', end: '', servicesToProvide: []});
  readonly editForm = form(this.editModel, path => {
    required(path.start);
    required(path.end);
  }, {submission: {action: async () => { this.saveEdit(); }}});
  readonly editableServices = computed<ServiceType[]>(() =>
    this.applicationDataStore.applicationData()?.availableServices.map(service => ({
      id: service.code,
      code: service.code,
      name: service.name,
      description: service.description,
      clientRateCode: service.clientRateCode,
      therapistRateCode: service.therapistRateCode,
    })) ?? [],
  );

  ngOnInit(): void {
    this.loadVisibleMonth();
  }

  get monthLabel(): string {
    return this.month().toLocaleDateString(this.locale, {month: 'long', year: 'numeric'});
  }

  get locale(): string {
    return this.languageService.currentLanguage() === 'fr' ? 'fr-CA' : 'en-CA';
  }

  get calendarWeeks(): Date[][] {
    const first = this.month();
    const start = new Date(first.getFullYear(), first.getMonth(), 1 - first.getDay());
    const days = Array.from({length: 42}, (_, index) => new Date(start.getFullYear(), start.getMonth(), start.getDate() + index));
    return Array.from({length: 6}, (_, index) => days.slice(index * 7, index * 7 + 7));
  }

  previousMonth(): void {
    const date = this.month();
    this.month.set(new Date(date.getFullYear(), date.getMonth() - 1, 1));
    this.loadVisibleMonth();
  }

  nextMonth(): void {
    const date = this.month();
    this.month.set(new Date(date.getFullYear(), date.getMonth() + 1, 1));
    this.loadVisibleMonth();
  }

  private loadVisibleMonth(): void {
    const firstVisibleDay = this.calendarWeeks[0][0];
    const lastVisibleDay = this.calendarWeeks[5][6];
    this.timeslotStore.loadCalendarData(this.formatApiDate(firstVisibleDay), this.formatApiDate(lastVisibleDay));
  }

  private formatApiDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
  }

  isCurrentMonth(day: Date): boolean {
    return day.getMonth() === this.month().getMonth();
  }

  slotsFor(day: Date): Timeslot[] {
    return this.slots().filter(slot => this.sameDay(new Date(slot.startDateTime), day))
      .sort((a, b) => new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime());
  }

  appointmentsFor(day: Date): Appointment[] {
    return this.appointments().filter(appointment => {
      const date = this.appointmentDate(appointment);
      return date !== null && this.sameDay(date, day);
    });
  }

  appointmentDate(appointment: Appointment): Date | null {
    const dateValue = appointment.startDateTime ?? appointment.date;
    if (!dateValue) return null;
    if (typeof dateValue === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
      const [year, month, day] = dateValue.split('-').map(Number);
      return new Date(year, month - 1, day);
    }
    const parsed = new Date(dateValue);
    return Number.isNaN(parsed.getTime()) ? null : parsed;
  }

  appointmentTime(appointment: Appointment): string {
    if (appointment.startDateTime && appointment.endDateTime) {
      return `${new Date(appointment.startDateTime).toLocaleTimeString(this.locale, {hour: 'numeric', minute: '2-digit'})} – ${new Date(appointment.endDateTime).toLocaleTimeString(this.locale, {hour: 'numeric', minute: '2-digit'})}`;
    }
    return appointment.slot || 'Time not specified';
  }

  isCancelledAppointment(appointment: Appointment): boolean {
    const status = String(appointment.status ?? appointment.appointmentstatus ?? '').trim().toUpperCase();
    return status === 'CANCELLED' || status === 'CANCELED' || status === 'CNL';
  }

  appointmentClass(appointment: Appointment): string {
    return this.isCancelledAppointment(appointment) ? 'appointment cancelled' : 'appointment';
  }

  appointmentLabel(appointment: Appointment): string {
    return this.isCancelledAppointment(appointment) ? 'calendar.status.cancelled' : 'calendar.appointment';
  }

  openAppointmentDetails(appointment: Appointment): void {
    this.selectedAppointment.set(appointment);
    this.selectedSlot.set(null);
  }

  closeAppointmentDetails(): void {
    this.selectedAppointment.set(null);
  }

  openDetails(slot: Timeslot): void {
    this.selectedAppointment.set(null);
    this.selectedSlot.set(slot);
    this.editing.set(false);
  }

  closeDetails(): void {
    this.selectedSlot.set(null);
    this.selectedAppointment.set(null);
    this.editing.set(false);
  }

  beginEdit(): void {
    const slot = this.selectedSlot();
    if (!slot || this.normalizeStatus(slot.status) !== TimeslotStatus.PROPOSED) return;
    this.editModel.set({
      start: this.toLocalInput(new Date(slot.startDateTime)),
      end: this.toLocalInput(new Date(slot.endDateTime)),
      servicesToProvide: (slot.servicesToProvide ?? []).map(service => ({...service})),
    });
    this.editing.set(true);
  }

  toggleEditService(service: ServiceType): void {
    this.editModel.update(model => ({
      ...model,
      servicesToProvide: model.servicesToProvide.some(selected => selected.id === service.id)
        ? model.servicesToProvide.filter(selected => selected.id !== service.id)
        : [...model.servicesToProvide, {...service}],
    }));
  }

  saveEdit(): void {
    const slot = this.selectedSlot();
    const {start, end} = this.editModel();
    if (!slot?.id || !start || !end) return;
    const startDateTime = new Date(start);
    const endDateTime = new Date(end);
    if (Number.isNaN(startDateTime.getTime()) || endDateTime <= startDateTime) {
      this.error.set('calendar.errors.endAfterStart');
      return;
    }
    this.error.set('');
    this.timeslotStore.updateTimeslot({
      ...slot,
      startDateTime,
      endDateTime,
      servicesToProvide: this.editModel().servicesToProvide.map(service => ({...service, code: service.code ?? service.id})),
    }).subscribe({
      next: updated => {
        this.selectedSlot.set(updated);
        this.editing.set(false);
      },
      error: () => {
        this.error.set('calendar.errors.update');
      }
    });
  }

  statusName(slot: Timeslot): string {
    switch (this.normalizeStatus(slot.status)) {
      case TimeslotStatus.ACCEPTED: return 'calendar.status.approved';
      case TimeslotStatus.REJECTED: return 'calendar.status.denied';
      default: return 'calendar.status.pending';
    }
  }

  statusClass(slot: Timeslot): string {
    return this.normalizeStatus(slot.status).toLowerCase();
  }

  durationMinutes(slot: Timeslot): number {
    return Math.round((new Date(slot.endDateTime).getTime() - new Date(slot.startDateTime).getTime()) / 60_000);
  }

  private normalizeStatus(status: TimeslotStatus | string | undefined): TimeslotStatus {
    const value = String(status ?? '').toUpperCase();
    if (value === 'ACCEPTED' || value === '1') return TimeslotStatus.ACCEPTED;
    if (value === 'REJECTED' || value === '2') return TimeslotStatus.REJECTED;
    return TimeslotStatus.PROPOSED;
  }

  private sameDay(left: Date, right: Date): boolean {
    return left.getFullYear() === right.getFullYear() && left.getMonth() === right.getMonth() && left.getDate() === right.getDate();
  }

  private toLocalInput(date: Date): string {
    const local = new Date(date.getTime() - date.getTimezoneOffset() * 60_000);
    return local.toISOString().slice(0, 16);
  }
}
