import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

import { EntryComponents, TestingModule } from 'src/testing/utils';
import {
  CasesExtendedService,
  CasesDetailsExtendedComponent,
  CasesListExtendedComponent,
  CasesNewExtendedComponent,
} from '../';
import { ICases } from 'src/app/entities/cases';
import { ListFiltersComponent, ServiceUtils } from 'src/app/common/shared';
import { ListComponent, DetailsComponent, NewComponent, FieldsComp } from 'src/app/common/general-components';

describe('CasesListExtendedComponent', () => {
  let fixture: ComponentFixture<CasesListExtendedComponent>;
  let component: CasesListExtendedComponent;
  let el: HTMLElement;

  describe('Unit tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [CasesListExtendedComponent, ListComponent],
        imports: [TestingModule],
        providers: [CasesExtendedService, ChangeDetectorRef],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CasesListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          CasesListExtendedComponent,
          CasesNewExtendedComponent,
          NewComponent,
          CasesDetailsExtendedComponent,
          ListComponent,
          DetailsComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'cases', component: CasesListExtendedComponent },
            { path: 'cases/:id', component: CasesDetailsExtendedComponent },
          ]),
        ],
        providers: [CasesExtendedService, ChangeDetectorRef],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CasesListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
