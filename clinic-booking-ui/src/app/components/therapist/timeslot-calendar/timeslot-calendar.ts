import {DatePipe, NgClass} from '@angular/common';
import {Component, OnInit, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';
import {LanguageService} from '../../../services/language.service';
import {CdkTrapFocus} from '@angular/cdk/a11y';
import {TimeSlot, TimeSlotStatus} from '../../../models/time-slot';
import {Appointment} from '../../../models/appointment';
import {TherapistService} from '../../../services/therapist.service';

@Component({
  standalone: true,
  selector: 'app-timeslot-calendar',
  imports: [DatePipe, FormsModule, NgClass, TranslatePipe, CdkTrapFocus],
  templateUrl: './timeslot-calendar.html',
  styleUrl: './timeslot-calendar.scss',
})
export class TimeslotCalendar implements OnInit {
  private readonly therapistService = inject(TherapistService);
  readonly languageService = inject(LanguageService);

  constructor() {
    this.languageService.setPageTitle('calendar.documentTitle');
  }
  readonly slots = signal<TimeSlot[]>([]);
  readonly appointments = signal<Appointment[]>([]);
  readonly month = signal(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
  readonly selectedSlot = signal<TimeSlot | null>(null);
  readonly selectedAppointment = signal<Appointment | null>(null);
  readonly loading = signal(true);
  readonly error = signal('');
  readonly saving = signal(false);
  readonly editing = signal(false);
  editStart = '';
  editEnd = '';

  ngOnInit(): void {
    this.loadSlots();
  }

  loadSlots(): void {
    this.loading.set(true);
    this.error.set('');
    this.therapistService.getTimeSlots().subscribe({
      next: (slots) => {
        this.slots.set((slots ?? []).map(slot => ({
          ...slot,
          startDateTime: new Date(slot.startDateTime),
          endDateTime: new Date(slot.endDateTime),
          status: this.normalizeStatus(slot.status),
        })));
        this.therapistService.getAppointments().subscribe({
          next: appointments => {
            this.appointments.set((appointments ?? []).filter(appointment => this.appointmentDate(appointment) !== null));
            this.loading.set(false);
          },
          error: () => {
            this.appointments.set([]);
            this.error.set('calendar.errors.appointments');
            this.loading.set(false);
          }
        });
      },
      error: () => {
        this.error.set('calendar.errors.load');
        this.loading.set(false);
      }
    });
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
  }

  nextMonth(): void {
    const date = this.month();
    this.month.set(new Date(date.getFullYear(), date.getMonth() + 1, 1));
  }

  isCurrentMonth(day: Date): boolean {
    return day.getMonth() === this.month().getMonth();
  }

  slotsFor(day: Date): TimeSlot[] {
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

  openAppointmentDetails(appointment: Appointment): void {
    this.selectedAppointment.set(appointment);
    this.selectedSlot.set(null);
  }

  closeAppointmentDetails(): void {
    this.selectedAppointment.set(null);
  }

  openDetails(slot: TimeSlot): void {
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
    if (!slot || this.normalizeStatus(slot.status) !== TimeSlotStatus.PROPOSED) return;
    this.editStart = this.toLocalInput(new Date(slot.startDateTime));
    this.editEnd = this.toLocalInput(new Date(slot.endDateTime));
    this.editing.set(true);
  }

  saveEdit(): void {
    const slot = this.selectedSlot();
    if (!slot?.id || !this.editStart || !this.editEnd) return;
    const startDateTime = new Date(this.editStart);
    const endDateTime = new Date(this.editEnd);
    if (Number.isNaN(startDateTime.getTime()) || endDateTime <= startDateTime) {
      this.error.set('calendar.errors.endAfterStart');
      return;
    }
    this.saving.set(true);
    this.error.set('');
    this.therapistService.updateTimeslot({...slot, startDateTime, endDateTime}).subscribe({
      next: updated => {
        const normalized = {...updated, startDateTime: new Date(updated.startDateTime), endDateTime: new Date(updated.endDateTime)};
        this.slots.update(items => items.map(item => item.id === normalized.id ? normalized : item));
        this.selectedSlot.set(normalized);
        this.editing.set(false);
        this.saving.set(false);
      },
      error: () => {
        this.error.set('calendar.errors.update');
        this.saving.set(false);
      }
    });
  }

  statusName(slot: TimeSlot): string {
    switch (this.normalizeStatus(slot.status)) {
      case TimeSlotStatus.ACCEPTED: return 'calendar.status.approved';
      case TimeSlotStatus.REJECTED: return 'calendar.status.denied';
      default: return 'calendar.status.pending';
    }
  }

  statusClass(slot: TimeSlot): string {
    return this.normalizeStatus(slot.status).toLowerCase();
  }

  serviceNames(slot: TimeSlot): string {
    const labels: Record<string, string> = this.languageService.currentLanguage() === 'fr'
      ? {BUCCAL_MASSAGE: 'Massage buccal', LYMPHATIC_DRAINAGE: 'Drainage lymphatique', SPORTS_MASSAGE: 'Massage sportif', SPECIAL_MASSAGE: 'Massage spécialisé', THAI_MASSAGE: 'Massage thaïlandais'}
      : {BUCCAL_MASSAGE: 'Buccal Massage', LYMPHATIC_DRAINAGE: 'Lymphatic Massage', SPORTS_MASSAGE: 'Sports Massage', SPECIAL_MASSAGE: 'Special Massage', THAI_MASSAGE: 'Thai Massage'};
    return (slot.servicesToProvide ?? []).map(service => labels[service.code ?? service.id ?? ''] ?? service.name ?? service.id ?? 'Service').join(', ')
      || (this.languageService.currentLanguage() === 'fr' ? 'Aucun soin indiqué' : 'No services listed');
  }

  durationMinutes(slot: TimeSlot): number {
    return Math.round((new Date(slot.endDateTime).getTime() - new Date(slot.startDateTime).getTime()) / 60_000);
  }

  private normalizeStatus(status: TimeSlotStatus | string | undefined): TimeSlotStatus {
    const value = String(status ?? '').toUpperCase();
    if (value === 'ACCEPTED' || value === '1') return TimeSlotStatus.ACCEPTED;
    if (value === 'REJECTED' || value === '2') return TimeSlotStatus.REJECTED;
    return TimeSlotStatus.PROPOSED;
  }

  private sameDay(left: Date, right: Date): boolean {
    return left.getFullYear() === right.getFullYear() && left.getMonth() === right.getMonth() && left.getDate() === right.getDate();
  }

  private toLocalInput(date: Date): string {
    const local = new Date(date.getTime() - date.getTimezoneOffset() * 60_000);
    return local.toISOString().slice(0, 16);
  }
}
