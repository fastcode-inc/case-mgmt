import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

import { EntryComponents, TestingModule } from 'src/testing/utils';
import {
  PersonExtendedService,
  PersonDetailsExtendedComponent,
  PersonListExtendedComponent,
  PersonNewExtendedComponent,
} from '../';
import { IPerson } from 'src/app/entities/person';
import { ListFiltersComponent, ServiceUtils } from 'src/app/common/shared';
import { ListComponent, DetailsComponent, NewComponent, FieldsComp } from 'src/app/common/general-components';

describe('PersonListExtendedComponent', () => {
  let fixture: ComponentFixture<PersonListExtendedComponent>;
  let component: PersonListExtendedComponent;
  let el: HTMLElement;

  describe('Unit tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [PersonListExtendedComponent, ListComponent],
        imports: [TestingModule],
        providers: [PersonExtendedService, ChangeDetectorRef],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          PersonListExtendedComponent,
          PersonNewExtendedComponent,
          NewComponent,
          PersonDetailsExtendedComponent,
          ListComponent,
          DetailsComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'person', component: PersonListExtendedComponent },
            { path: 'person/:id', component: PersonDetailsExtendedComponent },
          ]),
        ],
        providers: [PersonExtendedService, ChangeDetectorRef],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonListExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
