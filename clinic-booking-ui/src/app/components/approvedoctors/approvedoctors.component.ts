import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';

import {Doctor} from 'src/app/models/doctor';
import {DoctorService} from 'src/app/services/doctor.service';

@Component({
  selector: 'app-approvedoctors',
  templateUrl: './approvedoctors.component.html',
  styleUrls: ['./approvedoctors.component.css']
})
export class ApprovedoctorsComponent implements OnInit {
  currRole = '';
  loggedUser = '';
  doctors: Observable<Doctor[]> | undefined;
  responses: Observable<any> | undefined;
  showAcceptButton = true;
  showRejectButton = true;
  showAcceptedButton = false;
  showRejectedButton = false;

  constructor(private _service: DoctorService) {
  }

  ngOnInit(): void {
    this.loggedUser = JSON.stringify(sessionStorage.getItem('loggedUser') || '{}');
    this.loggedUser = this.loggedUser.replace(/"/g, '');

    this.currRole = JSON.stringify(sessionStorage.getItem('ROLE') || '{}');
    this.currRole = this.currRole.replace(/"/g, '');

    this.doctors = this._service.getDoctorList();
  }

  acceptRequest(curremail: string) {
    this.responses = this._service.acceptRequestForDoctorApproval(curremail);
    this.showAcceptButton = false;
    this.showRejectButton = false;
    this.showAcceptedButton = true;
    this.showRejectedButton = false;
  }

  rejectRequest(curremail: string) {
    this.responses = this._service.rejectRequestForDoctorApproval(curremail);
    this.showAcceptButton = false;
    this.showRejectButton = false;
    this.showAcceptedButton = false;
    this.showRejectedButton = true;
  }
}
