import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {Doctor} from 'src/app/models/doctor';
import {User} from 'src/app/models/user';
import {DoctorService} from 'src/app/services/doctor.service';
import {RegistrationService} from 'src/app/services/registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  user = new User();
  doctor = new Doctor();
  msg = ' ';
  showUserForm = false;
  showDoctorForm = false;

  constructor(private _registrationService: RegistrationService, private _doctorService: DoctorService, private _router: Router) {
  }

  ngOnInit(): void {
  }

  handleUserTabClicked() {
    this.showUserForm = true;
    this.showDoctorForm = false;
  }

  handleDoctorTabClicked() {
    this.showDoctorForm = true;
    this.showUserForm = false;
  }

  registerUser() {
    console.log('Submitting user registration', this.user);
    this._registrationService.registerUserFromRemote(this.user).subscribe({
      next: data => {
        console.log('Registration Success', data);
        sessionStorage.setItem('username', this.user.username);
        sessionStorage.setItem('gender', this.user.gender);
        this.msg = '';
        this._router.navigate(['/registrationsuccess']);
      }, error: err => {
        console.error('Registration Failed', err);
        this.msg = (err?.error || '').toString() || `User with ${this.user.email} already exists`;
      }
    });
  }

  registerDoctor() {
    console.log('Submitting doctor registration', this.doctor);
    this._registrationService.registerDoctorFromRemote(this.doctor).subscribe({
      next: data => {
        console.log('Doctor Registration Success', data);
        sessionStorage.setItem('doctorname', this.doctor.doctorname);
        sessionStorage.setItem('gender', this.doctor.gender);
        this.msg = '';
        this._router.navigate(['/registrationsuccess']);
      }, error: err => {
        console.error('Doctor Registration Failed', err);
        this.msg = (err?.error || '').toString() || `Doctor with ${this.doctor.email} already exists`;
      }
    });
  }
}
