import { NgModule } from '@angular/core';

import { CaseHistoryDetailsComponent, CaseHistoryListComponent, CaseHistoryNewComponent } from './';
import { caseHistoryRoute } from './case-history.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [CaseHistoryDetailsComponent, CaseHistoryListComponent, CaseHistoryNewComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [caseHistoryRoute, SharedModule, GeneralComponentsModule],
})
export class CaseHistoryModule {}
