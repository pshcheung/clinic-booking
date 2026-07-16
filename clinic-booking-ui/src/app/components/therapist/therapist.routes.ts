import {Routes} from '@angular/router';

import {canActivateAuthRole} from '../../auth/auth-role.guard';
import {TherapistDashboard} from './therapist-dashboard/therapist-dashboard';
import {AddTimeslot} from './add-timeslot/add-timeslot';
import {TherapistHome} from './therapist-home/therapist-home';
import {TimeslotStore} from '../../stores/timeslot-store';

export const routes: Routes =
  [
    {
      path: '', component: TherapistHome,
    },
    {
      path: 'add-timeslot',
      component: AddTimeslot,
      canActivate: [canActivateAuthRole],
      data: {role: ['clinic_admin_role'], organization: 'clinic-one'}
    },
  ];
