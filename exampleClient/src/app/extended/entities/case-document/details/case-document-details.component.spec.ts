import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import {
  CaseDocumentExtendedService,
  CaseDocumentDetailsExtendedComponent,
  CaseDocumentListExtendedComponent,
} from '../';
import { ICaseDocument } from 'src/app/entities/case-document';
describe('CaseDocumentDetailsExtendedComponent', () => {
  let component: CaseDocumentDetailsExtendedComponent;
  let fixture: ComponentFixture<CaseDocumentDetailsExtendedComponent>;
  let el: HTMLElement;

  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [CaseDocumentDetailsExtendedComponent, DetailsComponent],
        imports: [TestingModule],
        providers: [CaseDocumentExtendedService],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseDocumentDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          CaseDocumentDetailsExtendedComponent,
          CaseDocumentListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'casedocument', component: CaseDocumentDetailsExtendedComponent },
            { path: 'casedocument/:id', component: CaseDocumentListExtendedComponent },
          ]),
        ],
        providers: [CaseDocumentExtendedService],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseDocumentDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
