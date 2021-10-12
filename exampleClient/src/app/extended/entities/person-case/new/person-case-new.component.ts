import { Component, OnInit, Inject } from '@angular/core';
import { PersonCaseExtendedService } from '../person-case.service';

import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { PersonExtendedService } from 'src/app/extended/entities/person/person.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { PersonCaseNewComponent } from 'src/app/entities/person-case/index';

@Component({
  selector: 'app-person-case-new',
  templateUrl: './person-case-new.component.html',
  styleUrls: ['./person-case-new.component.scss'],
})
export class PersonCaseNewExtendedComponent extends PersonCaseNewComponent implements OnInit {
  title: string = 'New PersonCase';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<PersonCaseNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public personCaseExtendedService: PersonCaseExtendedService,
    public errorService: ErrorService,
    public casesExtendedService: CasesExtendedService,
    public personExtendedService: PersonExtendedService,
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
      personCaseExtendedService,
      errorService,
      casesExtendedService,
      personExtendedService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
