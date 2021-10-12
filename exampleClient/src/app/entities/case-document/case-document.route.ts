import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
// import { AuthGuard } from 'src/app/core/guards/auth.guard';

// import { CaseDocumentDetailsComponent, CaseDocumentListComponent, CaseDocumentNewComponent } from './';

const routes: Routes = [
  // { path: '', component: CaseDocumentListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: ':id', component: CaseDocumentDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  // { path: 'new', component: CaseDocumentNewComponent, canActivate: [ AuthGuard ] },
];

export const caseDocumentRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);
