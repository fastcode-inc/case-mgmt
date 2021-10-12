import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import { PersonCaseExtendedService, PersonCaseDetailsExtendedComponent, PersonCaseListExtendedComponent } from '../';
import { IPersonCase } from 'src/app/entities/person-case';
describe('PersonCaseDetailsExtendedComponent', () => {
  let component: PersonCaseDetailsExtendedComponent;
  let fixture: ComponentFixture<PersonCaseDetailsExtendedComponent>;
  let el: HTMLElement;

  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [PersonCaseDetailsExtendedComponent, DetailsComponent],
        imports: [TestingModule],
        providers: [PersonCaseExtendedService],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonCaseDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          PersonCaseDetailsExtendedComponent,
          PersonCaseListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'personcase', component: PersonCaseDetailsExtendedComponent },
            { path: 'personcase/:id', component: PersonCaseListExtendedComponent },
          ]),
        ],
        providers: [PersonCaseExtendedService],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonCaseDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
