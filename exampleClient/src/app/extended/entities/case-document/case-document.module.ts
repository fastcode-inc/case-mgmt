import { NgModule } from '@angular/core';

import {
  CaseDocumentExtendedService,
  CaseDocumentDetailsExtendedComponent,
  CaseDocumentListExtendedComponent,
  CaseDocumentNewExtendedComponent,
} from './';
import { CaseDocumentService } from 'src/app/entities/case-document';
import { CaseDocumentModule } from 'src/app/entities/case-document/case-document.module';
import { caseDocumentRoute } from './case-document.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [
  CaseDocumentDetailsExtendedComponent,
  CaseDocumentListExtendedComponent,
  CaseDocumentNewExtendedComponent,
];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [caseDocumentRoute, CaseDocumentModule, SharedModule, GeneralComponentsExtendedModule],
  providers: [{ provide: CaseDocumentService, useClass: CaseDocumentExtendedService }],
})
export class CaseDocumentExtendedModule {}
