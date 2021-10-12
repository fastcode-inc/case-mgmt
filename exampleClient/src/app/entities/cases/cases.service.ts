import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ICases } from './icases';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class CasesService extends GenericApiService<ICases> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'cases');
  }
}
