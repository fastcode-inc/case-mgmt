import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IPersonCase } from './iperson-case';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class PersonCaseService extends GenericApiService<IPersonCase> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'personCase');
  }
}
