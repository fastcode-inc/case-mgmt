import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { IPersonCase } from '../iperson-case';
import { PersonCaseService } from '../person-case.service';
import { Router, ActivatedRoute } from '@angular/router';
import { PersonCaseNewComponent } from '../new/person-case-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';
import { PersonService } from 'src/app/entities/person/person.service';

@Component({
  selector: 'app-person-case-list',
  templateUrl: './person-case-list.component.html',
  styleUrls: ['./person-case-list.component.scss'],
})
export class PersonCaseListComponent extends BaseListComponent<IPersonCase> implements OnInit {
  title = 'PersonCase';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public personCaseService: PersonCaseService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public personService: PersonService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, personCaseService, errorService);
  }

  ngOnInit() {
    this.entityName = 'PersonCase';
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = ['caseId', 'personId'];
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
        url: 'personCases',
        listColumn: 'cases',
        label: 'cases',
      },
      {
        column: [
          {
            key: 'personId',
            value: undefined,
            referencedkey: 'personId',
          },
        ],
        isParent: false,
        descriptiveField: 'personDescriptiveField',
        referencedDescriptiveField: 'personId',
        service: this.personService,
        associatedObj: undefined,
        table: 'person',
        type: 'ManyToOne',
        url: 'personCases',
        listColumn: 'person',
        label: 'person',
      },
    ];
  }

  setColumns() {
    this.columns = [
      {
        column: 'casesDescriptiveField',
        searchColumn: 'cases',
        label: 'cases',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'personDescriptiveField',
        searchColumn: 'person',
        label: 'person',
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
      comp = PersonCaseNewComponent;
    }
    super.addNew(comp);
  }
}
