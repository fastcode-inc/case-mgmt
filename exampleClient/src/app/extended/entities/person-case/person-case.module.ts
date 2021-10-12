import { NgModule } from '@angular/core';

import {
  PersonCaseExtendedService,
  PersonCaseDetailsExtendedComponent,
  PersonCaseListExtendedComponent,
  PersonCaseNewExtendedComponent,
} from './';
import { PersonCaseService } from 'src/app/entities/person-case';
import { PersonCaseModule } from 'src/app/entities/person-case/person-case.module';
import { personCaseRoute } from './person-case.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [PersonCaseDetailsExtendedComponent, PersonCaseListExtendedComponent, PersonCaseNewExtendedComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [personCaseRoute, PersonCaseModule, SharedModule, GeneralComponentsExtendedModule],
  providers: [{ provide: PersonCaseService, useClass: PersonCaseExtendedService }],
})
export class PersonCaseExtendedModule {}
