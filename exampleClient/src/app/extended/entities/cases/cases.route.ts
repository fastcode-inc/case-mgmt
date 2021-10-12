import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import { CasesDetailsExtendedComponent, CasesListExtendedComponent, CasesNewExtendedComponent } from './';

const routes: Routes = [
  { path: '', component: CasesListExtendedComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
  {
    path: ':id',
    component: CasesDetailsExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  { path: 'new', component: CasesNewExtendedComponent, canActivate: [AuthGuard] },
];

export const casesRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
