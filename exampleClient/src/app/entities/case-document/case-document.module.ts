import { NgModule } from '@angular/core';

import { CaseDocumentDetailsComponent, CaseDocumentListComponent, CaseDocumentNewComponent } from './';
import { caseDocumentRoute } from './case-document.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [CaseDocumentDetailsComponent, CaseDocumentListComponent, CaseDocumentNewComponent];
@NgModule({
  declarations: entities,
  exports: entities,
  imports: [caseDocumentRoute, SharedModule, GeneralComponentsModule],
})
export class CaseDocumentModule {}
