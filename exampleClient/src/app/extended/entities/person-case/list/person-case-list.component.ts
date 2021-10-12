import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Router, ActivatedRoute } from '@angular/router';

import { PersonCaseExtendedService } from '../person-case.service';
import { PersonCaseNewExtendedComponent } from '../new/person-case-new.component';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { CasesExtendedService } from 'src/app/extended/entities/cases/cases.service';
import { PersonExtendedService } from 'src/app/extended/entities/person/person.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';
import { PersonCaseListComponent } from 'src/app/entities/person-case/index';

@Component({
  selector: 'app-person-case-list',
  templateUrl: './person-case-list.component.html',
  styleUrls: ['./person-case-list.component.scss'],
})
export class PersonCaseListExtendedComponent extends PersonCaseListComponent implements OnInit {
  title: string = 'PersonCase';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public personCaseService: PersonCaseExtendedService,
    public errorService: ErrorService,
    public casesService: CasesExtendedService,
    public personService: PersonExtendedService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(
      router,
      route,
      global,
      dialog,
      changeDetectorRefs,
      pickerDialogService,
      personCaseService,
      errorService,
      casesService,
      personService,
      globalPermissionService
    );
  }

  ngOnInit() {
    super.ngOnInit();
  }

  addNew() {
    super.addNew(PersonCaseNewExtendedComponent);
  }
}
