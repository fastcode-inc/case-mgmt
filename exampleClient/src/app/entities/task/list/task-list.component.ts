import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ITask } from '../itask';
import { TaskService } from '../task.service';
import { Router, ActivatedRoute } from '@angular/router';
import { TaskNewComponent } from '../new/task-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
})
export class TaskListComponent extends BaseListComponent<ITask> implements OnInit {
  title = 'Task';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public taskService: TaskService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, taskService, errorService);
  }

  ngOnInit() {
    this.entityName = 'Task';
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = ['taskId'];
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
        url: 'tasks',
        listColumn: 'cases',
        label: 'cases',
      },
    ];
  }

  setColumns() {
    this.columns = [
      {
        column: 'username',
        searchColumn: 'username',
        label: 'username',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'type',
        searchColumn: 'type',
        label: 'type',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'taskId',
        searchColumn: 'taskId',
        label: 'task Id',
        sort: true,
        filter: true,
        type: ListColumnType.Number,
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
        column: 'message',
        searchColumn: 'message',
        label: 'message',
        sort: true,
        filter: true,
        type: ListColumnType.String,
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
      comp = TaskNewComponent;
    }
    super.addNew(comp);
  }
}
