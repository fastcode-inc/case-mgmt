import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
// import { AuthGuard } from 'src/app/core/guards/auth.guard';

// import { PersonDetailsComponent, PersonListComponent, PersonNewComponent } from './';

const routes: Routes = [
  // { path: '', component: PersonListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: ':id', component: PersonDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: 'new', component: PersonNewComponent, canActivate: [ AuthGuard ] },
];

export const personRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
