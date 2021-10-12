import { Component, OnInit, Inject } from '@angular/core';
import { CaseHistoryExtendedService } from '../case-history.service';

import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CaseHistoryNewComponent } from 'src/app/entities/case-history/index';

@Component({
  selector: 'app-case-history-new',
  templateUrl: './case-history-new.component.html',
  styleUrls: ['./case-history-new.component.scss'],
})
export class CaseHistoryNewExtendedComponent extends CaseHistoryNewComponent implements OnInit {
  title: string = 'New CaseHistory';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<CaseHistoryNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public caseHistoryExtendedService: CaseHistoryExtendedService,
    public errorService: ErrorService,
    public casesExtendedService: CasesExtendedService,
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
      caseHistoryExtendedService,
      errorService,
      casesExtendedService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
