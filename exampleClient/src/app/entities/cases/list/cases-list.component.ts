import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ICases } from '../icases';
import { CasesService } from '../cases.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CasesNewComponent } from '../new/cases-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

@Component({
  selector: 'app-cases-list',
  templateUrl: './cases-list.component.html',
  styleUrls: ['./cases-list.component.scss'],
})
export class CasesListComponent extends BaseListComponent<ICases> implements OnInit {
  title = 'Cases';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public casesService: CasesService,
    public errorService: ErrorService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, casesService, errorService);
  }

  ngOnInit() {
    this.entityName = 'Cases';
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = ['caseId'];
    super.ngOnInit();
  }

  setAssociation() {
    this.associations = [];
  }

  setColumns() {
    this.columns = [
      {
        column: 'type',
        searchColumn: 'type',
        label: 'type',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'summary',
        searchColumn: 'summary',
        label: 'summary',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'status',
        searchColumn: 'status',
        label: 'status',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'caseId',
        searchColumn: 'caseId',
        label: 'case Id',
        sort: true,
        filter: true,
        type: ListColumnType.Number,
      },
      {
        column: 'actions',
        label: 'Actions',
        sort: false,
        filter: false,
        type: ListColumnType.String,
      },
    ];
    this.selectedColumns = this.columns;
    this.displayedColumns = this.columns.map((obj) => {
      return obj.column;
    });
  }
  addNew(comp) {
    if (!comp) {
      comp = CasesNewComponent;
    }
    super.addNew(comp);
  }
}
