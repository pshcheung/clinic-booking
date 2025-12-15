import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';

import {Prescription} from 'src/app/models/prescription';
import {UserService} from 'src/app/services/user.service';

@Component({
  selector: 'app-prescriptionlist',
  templateUrl: './prescriptionlist.component.html',
  styleUrls: ['./prescriptionlist.component.css']
})
export class PrescriptionlistComponent implements OnInit {
  prescriptionlist: Observable<Prescription[]> | undefined;
  name: string = '';
  showMessageCard = false
  showPrescriptions = false;
  printClicked = false;

  constructor(private _service: UserService) {
  }

  ngOnInit(): void {
    this.showMessageCard = true;
    this.showPrescriptions = false;
  }

  searchPrescription() {
    this.prescriptionlist = this._service.getPrescriptionsByName(this.name);
    // $('#messagecard').hide();
    // $('#prescriptionpages').show();
    this.showMessageCard = false;
    this.showPrescriptions = true;
  }

  handlePrintClicked() {
    this.printClicked = true;
    window.print();
  }
}
