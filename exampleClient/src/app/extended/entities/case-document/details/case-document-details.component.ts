import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { CaseDocumentExtendedService } from '../case-document.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';

import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { CaseDocumentDetailsComponent } from 'src/app/entities/case-document/index';
import { FileService } from 'src/app/entities/case-document/file.service';

@Component({
  selector: 'app-case-document-details',
  templateUrl: './case-document-details.component.html',
  styleUrls: ['./case-document-details.component.scss'],
})
export class CaseDocumentDetailsExtendedComponent extends CaseDocumentDetailsComponent implements OnInit {
  title: string = 'CaseDocument';
  parentUrl: string = 'casedocument';
  //roles: IRole[];
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public caseDocumentExtendedService: CaseDocumentExtendedService,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public casesExtendedService: CasesExtendedService,
    public fileService: FileService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(
      formBuilder,
      router,
      route,
      dialog,
      caseDocumentExtendedService,
      pickerDialogService,
      errorService,
      casesExtendedService,
      fileService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
