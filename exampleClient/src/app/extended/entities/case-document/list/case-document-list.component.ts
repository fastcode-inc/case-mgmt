import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Router, ActivatedRoute } from '@angular/router';

import { CaseDocumentExtendedService } from '../case-document.service';
import { CaseDocumentNewExtendedComponent } from '../new/case-document-new.component';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { CaseDocumentListComponent } from 'src/app/entities/case-document/index';
import { FileService } from 'src/app/entities/case-document/file.service';

@Component({
  selector: 'app-case-document-list',
  templateUrl: './case-document-list.component.html',
  styleUrls: ['./case-document-list.component.scss'],
})
export class CaseDocumentListExtendedComponent extends CaseDocumentListComponent implements OnInit {
  title: string = 'CaseDocument';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public caseDocumentService: CaseDocumentExtendedService,
    public errorService: ErrorService,
    public casesService: CasesExtendedService,
    public fileService: FileService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(
      router,
      route,
      global,
      dialog,
      changeDetectorRefs,
      pickerDialogService,
      caseDocumentService,
      errorService,
      casesService,
      fileService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }

  addNew() {
    super.addNew(CaseDocumentNewExtendedComponent);
  }
}
