import { NgModule } from '@angular/core';

import { PersonCaseDetailsComponent, PersonCaseListComponent, PersonCaseNewComponent } from './';
import { personCaseRoute } from './person-case.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [PersonCaseDetailsComponent, PersonCaseListComponent, PersonCaseNewComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [personCaseRoute, SharedModule, GeneralComponentsModule],
})
export class PersonCaseModule {}
