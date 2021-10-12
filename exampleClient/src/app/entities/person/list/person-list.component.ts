import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { IPerson } from '../iperson';
import { PersonService } from '../person.service';
import { Router, ActivatedRoute } from '@angular/router';
import { PersonNewComponent } from '../new/person-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { UsersService } from 'src/app/admin/user-management/users/users.service';

@Component({
  selector: 'app-person-list',
  templateUrl: './person-list.component.html',
  styleUrls: ['./person-list.component.scss'],
})
export class PersonListComponent extends BaseListComponent<IPerson> implements OnInit {
  title = 'Person';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public personService: PersonService,
    public errorService: ErrorService,
    public usersService: UsersService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, personService, errorService);
  }

  ngOnInit() {
    this.entityName = 'Person';
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = ['personId'];
    super.ngOnInit();
  }

  setAssociation() {
    this.associations = [
      {
        column: [
          {
            key: 'username',
            value: undefined,
            referencedkey: 'username',
          },
        ],
        isParent: false,
        descriptiveField: 'usersDescriptiveField',
        referencedDescriptiveField: 'username',
        service: this.usersService,
        associatedObj: undefined,
        table: 'users',
        type: 'ManyToOne',
        url: 'persons',
        listColumn: 'users',
        label: 'users',
      },
    ];
  }

  setColumns() {
    this.columns = [
      {
        column: 'surname',
        searchColumn: 'surname',
        label: 'surname',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'personId',
        searchColumn: 'personId',
        label: 'person Id',
        sort: true,
        filter: true,
        type: ListColumnType.Number,
      },
      {
        column: 'homePhone',
        searchColumn: 'homePhone',
        label: 'home Phone',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'givenName',
        searchColumn: 'givenName',
        label: 'given Name',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'comments',
        searchColumn: 'comments',
        label: 'comments',
        sort: true,
        filter: true,
        type: ListColumnType.String,
      },
      {
        column: 'birthDate',
        searchColumn: 'birthDate',
        label: 'birth Date',
        sort: true,
        filter: true,
        type: ListColumnType.Date,
      },
      {
        column: 'usersDescriptiveField',
        searchColumn: 'users',
        label: 'users',
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
      comp = PersonNewComponent;
    }
    super.addNew(comp);
  }
}
