import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import { PersonExtendedService, PersonDetailsExtendedComponent, PersonListExtendedComponent } from '../';
import { IPerson } from 'src/app/entities/person';
describe('PersonDetailsExtendedComponent', () => {
  let component: PersonDetailsExtendedComponent;
  let fixture: ComponentFixture<PersonDetailsExtendedComponent>;
  let el: HTMLElement;

  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [PersonDetailsExtendedComponent, DetailsComponent],
        imports: [TestingModule],
        providers: [PersonExtendedService],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });

  describe('Integration Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          PersonDetailsExtendedComponent,
          PersonListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp,
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 'person', component: PersonDetailsExtendedComponent },
            { path: 'person/:id', component: PersonListExtendedComponent },
          ]),
        ],
        providers: [PersonExtendedService],
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonDetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  });
});
