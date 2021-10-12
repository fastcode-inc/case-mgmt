import { NgModule } from '@angular/core';

import { PersonDetailsComponent, PersonListComponent, PersonNewComponent } from './';
import { personRoute } from './person.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [PersonDetailsComponent, PersonListComponent, PersonNewComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [personRoute, SharedModule, GeneralComponentsModule],
})
export class PersonModule {}
