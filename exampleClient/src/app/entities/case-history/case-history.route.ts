import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
// import { AuthGuard } from 'src/app/core/guards/auth.guard';

// import { CaseHistoryDetailsComponent, CaseHistoryListComponent, CaseHistoryNewComponent } from './';

const routes: Routes = [
  // { path: '', component: CaseHistoryListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: ':id', component: CaseHistoryDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: 'new', component: CaseHistoryNewComponent, canActivate: [ AuthGuard ] },
];

export const caseHistoryRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
