import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';

import {Appointment} from 'src/app/models/appointment';
import {Slots} from 'src/app/models/slots';
import {DoctorService} from 'src/app/services/doctor.service';
import {UserService} from 'src/app/services/user.service';

@Component({
  selector: 'app-bookappointment',
  templateUrl: './bookappointment.component.html',
  styleUrls: ['./bookappointment.component.css']
})
export class BookappointmentComponent implements OnInit {
  currRole = '';
  loggedUser = '';
  message = '';
  appointment = new Appointment();
  slots: Observable<Slots[]> | undefined;
  doctors: Observable<Slots[]> | undefined;
  specializations: Observable<Slots[]> | undefined;
  showAppointmentForm = true;
  showMessageCard = false

  constructor(private _service: DoctorService, private _router: Router, private userService: UserService) {
  }

  ngOnInit(): void {
    this.loggedUser = JSON.stringify(sessionStorage.getItem('loggedUser') || '{}');
    this.loggedUser = this.loggedUser.replace(/"/g, '');

    this.currRole = JSON.stringify(sessionStorage.getItem('ROLE') || '{}');
    this.currRole = this.currRole.replace(/"/g, '');

    this.doctors = this._service.getSlotListWithUniqueDoctors();
    this.specializations = this._service.getSlotListWithUniqueSpecializations();
    this.slots = this._service.getSlotList();
  }

  bookAppointment() {
    this.userService.addBookingAppointments(this.appointment).subscribe(data => {
      console.log("appointment booked Successfully");
      this._router.navigate(['/userdashboard']);
    }, error => {
      console.log("process Failed");
      this.showAppointmentForm = false;
      this.showMessageCard = true;
      this.message = "There is a problem in Booking Your Appointment, Please check slot availability and try again !!!";
      console.log(error.error);
    })
  }


}
