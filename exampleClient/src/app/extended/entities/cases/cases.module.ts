import { NgModule } from '@angular/core';

import {
  CasesExtendedService,
  CasesDetailsExtendedComponent,
  CasesListExtendedComponent,
  CasesNewExtendedComponent,
} from './';
import { CasesService } from 'src/app/entities/cases';
import { CasesModule } from 'src/app/entities/cases/cases.module';
import { casesRoute } from './cases.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [CasesDetailsExtendedComponent, CasesListExtendedComponent, CasesNewExtendedComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [casesRoute, CasesModule, SharedModule, GeneralComponentsExtendedModule],
  providers: [{ provide: CasesService, useClass: CasesExtendedService }],
})
export class CasesExtendedModule {}
