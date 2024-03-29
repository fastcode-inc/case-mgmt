import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { CaseDocumentService } from '../case-document.service';
import { ICaseDocument } from '../icase-document';

import { BaseDetailsComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';
import { FileService } from '../file.service';

@Component({
  selector: 'app-case-document-details',
  templateUrl: './case-document-details.component.html',
  styleUrls: ['./case-document-details.component.scss'],
})
export class CaseDocumentDetailsComponent extends BaseDetailsComponent<ICaseDocument> implements OnInit {
  title = 'CaseDocument';
  parentUrl = 'casedocument';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public caseDocumentService: CaseDocumentService,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public fileService: FileService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(formBuilder, router, route, dialog, pickerDialogService, caseDocumentService, errorService);
  }

  ngOnInit() {
    this.entityName = 'CaseDocument';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.getItem();
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

  onItemFetched(item: ICaseDocument) {
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

    this.childAssociations = this.associations.filter((association) => {
      return association.isParent;
    });

    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let caseDocument = this.itemForm.getRawValue();
    super.onSubmit(caseDocument);
  }
}
