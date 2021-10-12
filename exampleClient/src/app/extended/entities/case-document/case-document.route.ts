import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import {
  CaseDocumentDetailsExtendedComponent,
  CaseDocumentListExtendedComponent,
  CaseDocumentNewExtendedComponent,
} from './';

const routes: Routes = [
  {
    path: '',
    component: CaseDocumentListExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  {
    path: ':id',
    component: CaseDocumentDetailsExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  { path: 'new', component: CaseDocumentNewExtendedComponent, canActivate: [AuthGuard] },
];

export const caseDocumentRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
