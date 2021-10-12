import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import { PersonDetailsExtendedComponent, PersonListExtendedComponent, PersonNewExtendedComponent } from './';

const routes: Routes = [
  { path: '', component: PersonListExtendedComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
  {
    path: ':id',
    component: PersonDetailsExtendedComponent,
    canDeactivate: [CanDeactivateGuard],
    canActivate: [AuthGuard],
  },
  { path: 'new', component: PersonNewExtendedComponent, canActivate: [AuthGuard] },
];

export const personRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
