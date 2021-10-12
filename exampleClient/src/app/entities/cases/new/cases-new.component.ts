import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CasesService } from '../cases.service';
import { ICases } from '../icases';
import { BaseNewComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

@Component({
  selector: 'app-cases-new',
  templateUrl: './cases-new.component.html',
  styleUrls: ['./cases-new.component.scss'],
})
export class CasesNewComponent extends BaseNewComponent<ICases> implements OnInit {
  title: string = 'New Cases';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<CasesNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public casesService: CasesService,
    public errorService: ErrorService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, casesService, errorService);
  }

  ngOnInit() {
    this.entityName = 'Cases';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.checkPassedData();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      caseId: ['', Validators.required],
      status: [''],
      summary: [''],
      type: [''],
    });

    this.fields = [
      {
        name: 'type',
        label: 'type',
        isRequired: false,
        isAutoGenerated: false,
        type: 'string',
      },
      {
        name: 'summary',
        label: 'summary',
        isRequired: false,
        isAutoGenerated: false,
        type: 'string',
      },
      {
        name: 'status',
        label: 'status',
        isRequired: false,
        isAutoGenerated: false,
        type: 'string',
      },
      {
        name: 'caseId',
        label: 'case Id',
        isRequired: true,
        isAutoGenerated: false,
        type: 'number',
      },
    ];
  }

  setAssociations() {
    this.associations = [];
    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let cases = this.itemForm.getRawValue();
    super.onSubmit(cases);
  }
}