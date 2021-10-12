import { NgModule } from '@angular/core';

import { CasesDetailsComponent, CasesListComponent, CasesNewComponent } from './';
import { casesRoute } from './cases.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [CasesDetailsComponent, CasesListComponent, CasesNewComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [casesRoute, SharedModule, GeneralComponentsModule],
})
export class CasesModule {}
