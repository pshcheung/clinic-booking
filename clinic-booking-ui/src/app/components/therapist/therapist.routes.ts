import {Routes} from '@angular/router';

import {canActivateAuthRole} from '../../auth/auth-role.guard';
import {AddTimeslot} from './add-timeslot/add-timeslot';
import {TimeslotCalendar} from './timeslot-calendar/timeslot-calendar';

export const routes: Routes =
  [
    {
      path: '', component: TimeslotCalendar,
    },
    {
      path: 'add-timeslot',
      component: AddTimeslot,
      canActivate: [canActivateAuthRole],
      data: {role: ['clinic_admin_role', 'clinic_manager_role', 'clinic_therapist_role'], organization: 'clinic-one'}
    },
  ];
