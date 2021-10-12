import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import {
  PersonCaseDetailsExtendedComponent,
  PersonCaseListExtendedComponent,
  PersonCaseNewExtendedComponent,
} from './';

const routes: Routes = [
  {
    path: '',
    component: PersonCaseListExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  {
    path: ':id',
    component: PersonCaseDetailsExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  { path: 'new', component: PersonCaseNewExtendedComponent, canActivate: [AuthGuard] },
];

export const personCaseRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
