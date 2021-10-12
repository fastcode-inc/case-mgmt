import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ICaseHistory } from './icase-history';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class CaseHistoryService extends GenericApiService<ICaseHistory> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'caseHistory');
  }
}
