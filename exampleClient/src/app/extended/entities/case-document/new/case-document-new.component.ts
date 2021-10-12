import { Component, OnInit, Inject } from '@angular/core';
import { CaseDocumentExtendedService } from '../case-document.service';

import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CaseDocumentNewComponent } from 'src/app/entities/case-document/index';
import { FileService } from 'src/app/entities/case-document/file.service';

@Component({
  selector: 'app-case-document-new',
  templateUrl: './case-document-new.component.html',
  styleUrls: ['./case-document-new.component.scss'],
})
export class CaseDocumentNewExtendedComponent extends CaseDocumentNewComponent implements OnInit {
  title: string = 'New CaseDocument';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<CaseDocumentNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public caseDocumentExtendedService: CaseDocumentExtendedService,
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
      dialogRef,
      data,
      pickerDialogService,
      caseDocumentExtendedService,
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
