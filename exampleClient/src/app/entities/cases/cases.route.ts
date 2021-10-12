import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
// import { AuthGuard } from 'src/app/core/guards/auth.guard';

// import { CasesDetailsComponent, CasesListComponent, CasesNewComponent } from './';

const routes: Routes = [
  // { path: '', component: CasesListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: ':id', component: CasesDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: 'new', component: CasesNewComponent, canActivate: [ AuthGuard ] },
];

export const casesRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
