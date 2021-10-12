import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

import { EntryComponents, TestingModule } from 'src/testing/utils';
import {
  PersonCaseExtendedService,
  PersonCaseDetailsExtendedComponent,
  PersonCaseListExtendedComponent,
  PersonCaseNewExtendedComponent,
} from '../';
import { IPersonCase } from 'src/app/entities/person-case';
import { ListFiltersComponent, ServiceUtils } from 'src/app/common/shared';
import { ListComponent, DetailsComponent, NewComponent, FieldsComp } from 'src/app/common/general-components';

describe('PersonCaseListExtendedComponent', () => {
  let fixture: ComponentFixture<PersonCaseListExtendedComponent>;
  let component: PersonCaseListExtendedComponent;
  let el: HTMLElement;

  describe('Unit tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [PersonCaseListExtendedComponent, ListComponent],
        imports: [TestingModule],
        providers: [PersonCaseExtendedService, ChangeDetectorRef],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonCaseListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          PersonCaseListExtendedComponent,
          PersonCaseNewExtendedComponent,
          NewComponent,
          PersonCaseDetailsExtendedComponent,
          ListComponent,
          DetailsComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'personcase', component: PersonCaseListExtendedComponent },
            { path: 'personcase/:id', component: PersonCaseDetailsExtendedComponent },
          ]),
        ],
        providers: [PersonCaseExtendedService, ChangeDetectorRef],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonCaseListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
