import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Therapist} from '../models/therapist';
import {Client} from '../models/client';
import {TimeSlot} from '../models/time-slot';

const NAV_URL = environment.apiURL;

@Injectable({
  providedIn: 'root'
})
export class TherapistService {
  client = new Client();
  therapist = new Therapist();
  private _http: HttpClient = inject(HttpClient);

  constructor() {
  }

  addTherapist(therapist: Therapist): Observable<any> {
    return this._http.post<any>(`${NAV_URL}/therapist`, therapist)
  }

  getTherapists(): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/therapist`);
  }

  getTimeSlots(): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/timeslot`);
  }

  getAvailableSlotListForTherapist(): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/timeslot/therapist`);
  }

  getSlotListForService(): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/timeslot/service`);
  }

  getRequestedTimeSlots(loggedUser: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/timeslot/` + loggedUser);
  }

  getDoctorListByEmail(loggedUser: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/doctorlistbyemail/` + loggedUser);
  }

  getPatientListByEmail(email: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/patientlistbyemail/` + email);
  }

  getPatientList(): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/patientlist`);
  }

  getPatientListByDoctorEmail(loggedUser: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/patientlistbydoctoremail/` + loggedUser);
  }

  getPatientListByDoctorEmailAndDate(loggedUser: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/patientlistbydoctoremailanddate/` + loggedUser);
  }

  public acceptRequestForDoctorApproval(email: string): Observable<any> {
    console.log("accepted");
    return this._http.get<any>(`${NAV_URL}/acceptstatus/` + email);
  }

  public rejectRequestForDoctorApproval(email: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/rejectstatus/` + email)
  }

  public acceptRequestForPatientApproval(slot: string): Observable<any> {
    console.log("accepted");
    return this._http.get<any>(`${NAV_URL}/acceptpatient/` + slot);
  }

  public rejectRequestForPatientApproval(slot: string): Observable<any> {
    return this._http.get<any>(`${NAV_URL}/rejectpatient/` + slot);
  }

  public addBookingSlots(slot: TimeSlot): Observable<any> {
    return this._http.post<any>(`${NAV_URL}/timeslot`, slot);
  }

  public getProfileDetails(loggedUser: string): Observable<any> {
    return this._http.get(`${NAV_URL}/doctorProfileDetails/` + loggedUser);
  }

  public UpdateDoctorProfile(user: any): Observable<any> {
    return this._http.put<any>(`${NAV_URL}/updatedoctor`, user)
  }

}
