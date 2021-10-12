import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
// import { AuthGuard } from 'src/app/core/guards/auth.guard';

// import { PersonCaseDetailsComponent, PersonCaseListComponent, PersonCaseNewComponent } from './';

const routes: Routes = [
  // { path: '', component: PersonCaseListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: ':id', component: PersonCaseDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: 'new', component: PersonCaseNewComponent, canActivate: [ AuthGuard ] },
];

export const personCaseRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
