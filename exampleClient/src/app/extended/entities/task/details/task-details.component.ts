import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { TaskExtendedService } from '../task.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';

import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { TaskDetailsComponent } from 'src/app/entities/task/index';

@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.scss'],
})
export class TaskDetailsExtendedComponent extends TaskDetailsComponent implements OnInit {
  title: string = 'Task';
  parentUrl: string = 'task';
  //roles: IRole[];
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public taskExtendedService: TaskExtendedService,
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
      taskExtendedService,
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
