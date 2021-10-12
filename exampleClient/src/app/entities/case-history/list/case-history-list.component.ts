import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ICaseHistory } from '../icase-history';
import { CaseHistoryService } from '../case-history.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CaseHistoryNewComponent } from '../new/case-history-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';

@Component({
  selector: 'app-case-history-list',
  templateUrl: './case-history-list.component.html',
  styleUrls: ['./case-history-list.component.scss'],
})
export class CaseHistoryListComponent extends BaseListComponent<ICaseHistory> implements OnInit {
  title = 'CaseHistory';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public caseHistoryService: CaseHistoryService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, caseHistoryService, errorService);
  }

  ngOnInit() {
    this.entityName = 'CaseHistory';
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = ['caseHistoryId'];
    super.ngOnInit();
  }

  setAssociation() {
    this.associations = [
      {
        column: [
          {
            key: 'caseId',
            value: undefined,
            referencedkey: 'caseId',
          },
        ],
        isParent: false,
        descriptiveField: 'casesDescriptiveField',
        referencedDescriptiveField: 'caseId',
        service: this.casesService,
        associatedObj: undefined,
        table: 'cases',
        type: 'ManyToOne',
        url: 'caseHistorys',
        listColumn: 'cases',
        label: 'cases',
      },
    ];
  }

  setColumns() {
    this.columns = [
      {
        column: 'timestamp',
        searchColumn: 'timestamp',
        label: 'timestamp',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'message',
        searchColumn: 'message',
        label: 'message',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'caseHistoryId',
        searchColumn: 'caseHistoryId',
        label: 'case History Id',
        sort: true,
        filter: true,
        type: ListColumnType.Number,
      },
      {
        column: 'casesDescriptiveField',
        searchColumn: 'cases',
        label: 'cases',
        sort: true,
        filter: true,
        type: ListColumnType.String,
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
      comp = CaseHistoryNewComponent;
    }
    super.addNew(comp);
  }
}
