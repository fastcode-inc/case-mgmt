import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ICaseDocument } from '../icase-document';
import { CaseDocumentService } from '../case-document.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CaseDocumentNewComponent } from '../new/case-document-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { CasesService } from 'src/app/entities/cases/cases.service';
import { FileService } from '../file.service';

@Component({
  selector: 'app-case-document-list',
  templateUrl: './case-document-list.component.html',
  styleUrls: ['./case-document-list.component.scss'],
})
export class CaseDocumentListComponent extends BaseListComponent<ICaseDocument> implements OnInit {
  title = 'CaseDocument';
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public caseDocumentService: CaseDocumentService,
    public errorService: ErrorService,
    public casesService: CasesService,
    public fileService: FileService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, caseDocumentService, errorService);
  }

  ngOnInit() {
    this.entityName = 'CaseDocument';
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = ['caseId', 'fileId'];
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
        url: 'caseDocuments',
        listColumn: 'cases',
        label: 'cases',
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
        descriptiveField: 'fileDescriptiveField',
        referencedDescriptiveField: 'name',
        service: this.fileService,
        associatedObj: undefined,
        table: 'file',
        type: 'ManyToOne',
        url: 'caseDocuments',
        listColumn: 'file',
        label: 'file',
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
        column: 'fileDescriptiveField',
        searchColumn: 'file',
        label: 'file',
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
      comp = CaseDocumentNewComponent;
    }
    super.addNew(comp);
  }
}
