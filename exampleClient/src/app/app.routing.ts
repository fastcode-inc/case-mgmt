import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { SwaggerComponent } from './core/swagger/swagger.component';
import { ErrorPageComponent } from './core/error-page/error-page.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./extended/core/core.module').then((m) => m.CoreExtendedModule),
  },
  { path: 'swagger-ui', component: SwaggerComponent, canActivate: [AuthGuard] },
  {
    path: '',
    loadChildren: () => import('./extended/admin/admin.module').then((m) => m.AdminExtendedModule),
  },
  {
    path: '',
    loadChildren: () => import('./extended/account/account.module').then((m) => m.AccountExtendedModule),
  },
  {
    path: 'reporting',
    loadChildren: () => import('./reporting-module/reporting.module').then((m) => m.ReportingModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'cases',
    loadChildren: () => import('./extended/entities/cases/cases.module').then((m) => m.CasesExtendedModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'casedocument',
    loadChildren: () =>
      import('./extended/entities/case-document/case-document.module').then((m) => m.CaseDocumentExtendedModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'personcase',
    loadChildren: () =>
      import('./extended/entities/person-case/person-case.module').then((m) => m.PersonCaseExtendedModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'task',
    loadChildren: () => import('./extended/entities/task/task.module').then((m) => m.TaskExtendedModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'person',
    loadChildren: () => import('./extended/entities/person/person.module').then((m) => m.PersonExtendedModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'casehistory',
    loadChildren: () =>
      import('./extended/entities/case-history/case-history.module').then((m) => m.CaseHistoryExtendedModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'scheduler',
    loadChildren: () => import('./scheduler/scheduler.module').then((m) => m.SchedulerModule),
    canActivate: [AuthGuard],
  },
  { path: '**', component: ErrorPageComponent },
];

export const routingModule: ModuleWithProviders<any> = RouterModule.forRoot(routes);
