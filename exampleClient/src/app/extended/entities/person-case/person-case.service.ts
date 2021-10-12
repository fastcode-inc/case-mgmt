import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PersonCaseService } from 'src/app/entities/person-case/person-case.service';
@Injectable({
  providedIn: 'root',
})
export class PersonCaseExtendedService extends PersonCaseService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
