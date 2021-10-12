import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { PersonCaseExtendedService } from '../person-case.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { PersonExtendedService } from 'src/app/extended/entities/person/person.service';

import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { PersonCaseDetailsComponent } from 'src/app/entities/person-case/index';

@Component({
  selector: 'app-person-case-details',
  templateUrl: './person-case-details.component.html',
  styleUrls: ['./person-case-details.component.scss'],
})
export class PersonCaseDetailsExtendedComponent extends PersonCaseDetailsComponent implements OnInit {
  title: string = 'PersonCase';
  parentUrl: string = 'personcase';
  //roles: IRole[];
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public personCaseExtendedService: PersonCaseExtendedService,
    public pickerDialogService: PickerDialogService,
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
      personCaseExtendedService,
      pickerDialogService,
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
