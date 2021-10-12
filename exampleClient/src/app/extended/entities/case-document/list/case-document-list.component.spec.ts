import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

import { EntryComponents, TestingModule } from 'src/testing/utils';
import {
  CaseDocumentExtendedService,
  CaseDocumentDetailsExtendedComponent,
  CaseDocumentListExtendedComponent,
  CaseDocumentNewExtendedComponent,
} from '../';
import { ICaseDocument } from 'src/app/entities/case-document';
import { ListFiltersComponent, ServiceUtils } from 'src/app/common/shared';
import { ListComponent, DetailsComponent, NewComponent, FieldsComp } from 'src/app/common/general-components';

describe('CaseDocumentListExtendedComponent', () => {
  let fixture: ComponentFixture<CaseDocumentListExtendedComponent>;
  let component: CaseDocumentListExtendedComponent;
  let el: HTMLElement;

  describe('Unit tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [CaseDocumentListExtendedComponent, ListComponent],
        imports: [TestingModule],
        providers: [CaseDocumentExtendedService, ChangeDetectorRef],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseDocumentListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          CaseDocumentListExtendedComponent,
          CaseDocumentNewExtendedComponent,
          NewComponent,
          CaseDocumentDetailsExtendedComponent,
          ListComponent,
          DetailsComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'casedocument', component: CaseDocumentListExtendedComponent },
            { path: 'casedocument/:id', component: CaseDocumentDetailsExtendedComponent },
          ]),
        ],
        providers: [CaseDocumentExtendedService, ChangeDetectorRef],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseDocumentListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
