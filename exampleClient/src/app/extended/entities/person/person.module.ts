import { NgModule } from '@angular/core';

import {
  PersonExtendedService,
  PersonDetailsExtendedComponent,
  PersonListExtendedComponent,
  PersonNewExtendedComponent,
} from './';
import { PersonService } from 'src/app/entities/person';
import { PersonModule } from 'src/app/entities/person/person.module';
import { personRoute } from './person.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [PersonDetailsExtendedComponent, PersonListExtendedComponent, PersonNewExtendedComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [personRoute, PersonModule, SharedModule, GeneralComponentsExtendedModule],
  providers: [{ provide: PersonService, useClass: PersonExtendedService }],
})
export class PersonExtendedModule {}
