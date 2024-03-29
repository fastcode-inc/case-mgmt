import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CaseDocumentService } from '../case-document.service';
import { ICaseDocument } from '../icase-document';
import { BaseNewComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';
import { FileService } from '../file.service';

@Component({
  selector: 'app-case-document-new',
  templateUrl: './case-document-new.component.html',
  styleUrls: ['./case-document-new.component.scss'],
})
export class CaseDocumentNewComponent extends BaseNewComponent<ICaseDocument> implements OnInit {
  title: string = 'New CaseDocument';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<CaseDocumentNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public caseDocumentService: CaseDocumentService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public fileService: FileService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, caseDocumentService, errorService);
  }

  ngOnInit() {
    this.entityName = 'CaseDocument';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.checkPassedData();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      caseId: ['', Validators.required],
      fileId: ['', Validators.required],
      casesDescriptiveField: [''],
      fileDescriptiveField: [''],
    });

    this.fields = [
    ];
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
            key: 'fileId',
            value: undefined,
            referencedkey: 'id',
          },
        ],
        isParent: false,
        table: 'file',
        type: 'ManyToOne',
        service: this.fileService,
        label: 'file',
        descriptiveField: 'fileDescriptiveField',
        referencedDescriptiveField: 'name',
      },
    ];
    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let caseDocument = this.itemForm.getRawValue();
    super.onSubmit(caseDocument);
  }
}
