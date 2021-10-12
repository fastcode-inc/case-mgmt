import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { CaseHistoryExtendedService } from '../case-history.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';

import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { CaseHistoryDetailsComponent } from 'src/app/entities/case-history/index';

@Component({
  selector: 'app-case-history-details',
  templateUrl: './case-history-details.component.html',
  styleUrls: ['./case-history-details.component.scss'],
})
export class CaseHistoryDetailsExtendedComponent extends CaseHistoryDetailsComponent implements OnInit {
  title: string = 'CaseHistory';
  parentUrl: string = 'casehistory';
  //roles: IRole[];
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public caseHistoryExtendedService: CaseHistoryExtendedService,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public casesExtendedService: CasesExtendedService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(
      formBuilder,
      router,
      route,
      dialog,
      caseHistoryExtendedService,
      pickerDialogService,
      errorService,
      casesExtendedService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
