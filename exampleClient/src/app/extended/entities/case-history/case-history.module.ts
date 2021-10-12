import { NgModule } from '@angular/core';

import {
  CaseHistoryExtendedService,
  CaseHistoryDetailsExtendedComponent,
  CaseHistoryListExtendedComponent,
  CaseHistoryNewExtendedComponent,
} from './';
import { CaseHistoryService } from 'src/app/entities/case-history';
import { CaseHistoryModule } from 'src/app/entities/case-history/case-history.module';
import { caseHistoryRoute } from './case-history.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [
  CaseHistoryDetailsExtendedComponent,
  CaseHistoryListExtendedComponent,
  CaseHistoryNewExtendedComponent,
];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [caseHistoryRoute, CaseHistoryModule, SharedModule, GeneralComponentsExtendedModule],
  providers: [{ provide: CaseHistoryService, useClass: CaseHistoryExtendedService }],
})
export class CaseHistoryExtendedModule {}
