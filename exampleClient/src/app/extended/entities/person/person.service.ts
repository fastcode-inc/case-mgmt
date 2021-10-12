import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PersonService } from 'src/app/entities/person/person.service';
@Injectable({
  providedIn: 'root',
})
export class PersonExtendedService extends PersonService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
