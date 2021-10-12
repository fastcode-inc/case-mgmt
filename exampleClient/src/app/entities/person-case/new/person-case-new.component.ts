import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { PersonCaseService } from '../person-case.service';
import { IPersonCase } from '../iperson-case';
import { BaseNewComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';
import { PersonService } from 'src/app/entities/person/person.service';

@Component({
  selector: 'app-person-case-new',
  templateUrl: './person-case-new.component.html',
  styleUrls: ['./person-case-new.component.scss'],
})
export class PersonCaseNewComponent extends BaseNewComponent<IPersonCase> implements OnInit {
  title: string = 'New PersonCase';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<PersonCaseNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public personCaseService: PersonCaseService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public personService: PersonService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, personCaseService, errorService);
  }

  ngOnInit() {
    this.entityName = 'PersonCase';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.checkPassedData();
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
        service: this.casesService,
        label: 'cases',
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
        service: this.personService,
        label: 'person',
        descriptiveField: 'personDescriptiveField',
        referencedDescriptiveField: 'personId',
      },
    ];
    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let personCase = this.itemForm.getRawValue();
    super.onSubmit(personCase);
  }
}
