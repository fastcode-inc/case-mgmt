import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { PersonCaseService } from '../person-case.service';
import { IPersonCase } from '../iperson-case';

import { BaseDetailsComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';
import { PersonService } from 'src/app/entities/person/person.service';

@Component({
  selector: 'app-person-case-details',
  templateUrl: './person-case-details.component.html',
  styleUrls: ['./person-case-details.component.scss'],
})
export class PersonCaseDetailsComponent extends BaseDetailsComponent<IPersonCase> implements OnInit {
  title = 'PersonCase';
  parentUrl = 'personcase';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public personCaseService: PersonCaseService,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public personService: PersonService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(formBuilder, router, route, dialog, pickerDialogService, personCaseService, errorService);
  }

  ngOnInit() {
    this.entityName = 'PersonCase';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.getItem();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      caseId: ['', Validators.required],
      personId: ['', Validators.required],
      casesDescriptiveField: [''],
      personDescriptiveField: [''],
    });

    this.fields = [];
  }

  onItemFetched(item: IPersonCase) {
    this.item = item;
    this.itemForm.patchValue(item);
  }

  setAssociations() {
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
        table: 'cases',
        type: 'ManyToOne',
        label: 'cases',
        service: this.casesService,
        descriptiveField: 'casesDescriptiveField',
        referencedDescriptiveField: 'caseId',
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
        table: 'person',
        type: 'ManyToOne',
        label: 'person',
        service: this.personService,
        descriptiveField: 'personDescriptiveField',
        referencedDescriptiveField: 'personId',
      },
    ];

    this.childAssociations = this.associations.filter((association) => {
      return association.isParent;
    });

    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let personCase = this.itemForm.getRawValue();
    super.onSubmit(personCase);
  }
}
