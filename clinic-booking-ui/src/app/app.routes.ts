import {Routes} from '@angular/router';

import {routes as TherapistRoutes} from '../app/components/therapist/therapist.routes'
import {canActivateAuthRole} from './auth/auth-role.guard';
import {AdminDashboard} from './components/admin/admin-dashboard/admin-dashboard';
import {Home} from './components/home/home';
import {TherapistDashboard} from './components/therapist/therapist-dashboard/therapist-dashboard';
import {ClientDashboard} from './components/client/client-dashboard/client-dashboard';
import {TimeslotStore} from './stores/timeslot-store';

export const routes: Routes =
  [
    {
      path: '', component: Home,
    },
    {
      path: 'admin', component: AdminDashboard, canActivate: [canActivateAuthRole],
      data: {role: ['clinic_admin_role'], organization: 'clinic-one'}
    },
    {
      path: 'therapist',
      component: TherapistDashboard,
      providers: [TimeslotStore],
      children: TherapistRoutes,
      canActivate: [canActivateAuthRole],
      data: {role: ['clinic_admin_role', 'clinic_manager_role', 'clinic_therapist_role'], organization: 'clinic-one'}
    },
    {
      path: 'client', component: ClientDashboard, canActivate: [canActivateAuthRole],
      data: {role: ['clinic_admin_role', 'clinic_client_role'], organization: 'clinic-one'}
    }
  ];
