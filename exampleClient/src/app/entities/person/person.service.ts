import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IPerson } from './iperson';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class PersonService extends GenericApiService<IPerson> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'person');
  }
}
