import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import { CaseHistoryExtendedService, CaseHistoryDetailsExtendedComponent, CaseHistoryListExtendedComponent } from '../';
import { ICaseHistory } from 'src/app/entities/case-history';
describe('CaseHistoryDetailsExtendedComponent', () => {
  let component: CaseHistoryDetailsExtendedComponent;
  let fixture: ComponentFixture<CaseHistoryDetailsExtendedComponent>;
  let el: HTMLElement;

  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [CaseHistoryDetailsExtendedComponent, DetailsComponent],
        imports: [TestingModule],
        providers: [CaseHistoryExtendedService],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseHistoryDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          CaseHistoryDetailsExtendedComponent,
          CaseHistoryListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'casehistory', component: CaseHistoryDetailsExtendedComponent },
            { path: 'casehistory/:id', component: CaseHistoryListExtendedComponent },
          ]),
        ],
        providers: [CaseHistoryExtendedService],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CaseHistoryDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
