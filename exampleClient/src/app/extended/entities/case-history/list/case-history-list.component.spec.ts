import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

import { EntryComponents, TestingModule } from 'src/testing/utils';
import {
  CaseHistoryExtendedService,
  CaseHistoryDetailsExtendedComponent,
  CaseHistoryListExtendedComponent,
  CaseHistoryNewExtendedComponent,
} from '../';
import { ICaseHistory } from 'src/app/entities/case-history';
import { ListFiltersComponent, ServiceUtils } from 'src/app/common/shared';
import { ListComponent, DetailsComponent, NewComponent, FieldsComp } from 'src/app/common/general-components';

describe('CaseHistoryListExtendedComponent', () => {
  let fixture: ComponentFixture<CaseHistoryListExtendedComponent>;
  let component: CaseHistoryListExtendedComponent;
  let el: HTMLElement;

  describe('Unit tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [CaseHistoryListExtendedComponent, ListComponent],
        imports: [TestingModule],
        providers: [CaseHistoryExtendedService, ChangeDetectorRef],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseHistoryListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          CaseHistoryListExtendedComponent,
          CaseHistoryNewExtendedComponent,
          NewComponent,
          CaseHistoryDetailsExtendedComponent,
          ListComponent,
          DetailsComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'casehistory', component: CaseHistoryListExtendedComponent },
            { path: 'casehistory/:id', component: CaseHistoryDetailsExtendedComponent },
          ]),
        ],
        providers: [CaseHistoryExtendedService, ChangeDetectorRef],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseHistoryListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
