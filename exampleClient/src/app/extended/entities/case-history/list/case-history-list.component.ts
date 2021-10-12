import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Router, ActivatedRoute } from '@angular/router';

import { CaseHistoryExtendedService } from '../case-history.service';
import { CaseHistoryNewExtendedComponent } from '../new/case-history-new.component';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { CaseHistoryListComponent } from 'src/app/entities/case-history/index';

@Component({
  selector: 'app-case-history-list',
  templateUrl: './case-history-list.component.html',
  styleUrls: ['./case-history-list.component.scss'],
})
export class CaseHistoryListExtendedComponent extends CaseHistoryListComponent implements OnInit {
  title: string = 'CaseHistory';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public caseHistoryService: CaseHistoryExtendedService,
    public errorService: ErrorService,
    public casesService: CasesExtendedService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(
      router,
      route,
      global,
      dialog,
      changeDetectorRefs,
      pickerDialogService,
      caseHistoryService,
      errorService,
      casesService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }

  addNew() {
    super.addNew(CaseHistoryNewExtendedComponent);
  }
}
