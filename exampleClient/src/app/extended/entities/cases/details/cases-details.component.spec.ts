import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import { CasesExtendedService, CasesDetailsExtendedComponent, CasesListExtendedComponent } from '../';
import { ICases } from 'src/app/entities/cases';
describe('CasesDetailsExtendedComponent', () => {
  let component: CasesDetailsExtendedComponent;
  let fixture: ComponentFixture<CasesDetailsExtendedComponent>;
  let el: HTMLElement;

  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [CasesDetailsExtendedComponent, DetailsComponent],
        imports: [TestingModule],
        providers: [CasesExtendedService],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CasesDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          CasesDetailsExtendedComponent,
          CasesListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'cases', component: CasesDetailsExtendedComponent },
            { path: 'cases/:id', component: CasesListExtendedComponent },
          ]),
        ],
        providers: [CasesExtendedService],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CasesDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
