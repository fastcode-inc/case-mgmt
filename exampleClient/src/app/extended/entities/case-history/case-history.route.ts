import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import {
  CaseHistoryDetailsExtendedComponent,
  CaseHistoryListExtendedComponent,
  CaseHistoryNewExtendedComponent,
} from './';

const routes: Routes = [
  {
    path: '',
    component: CaseHistoryListExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  {
    path: ':id',
    component: CaseHistoryDetailsExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  { path: 'new', component: CaseHistoryNewExtendedComponent, canActivate: [AuthGuard] },
];

export const caseHistoryRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
