import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CaseHistoryService } from 'src/app/entities/case-history/case-history.service';
@Injectable({
  providedIn: 'root',
})
export class CaseHistoryExtendedService extends CaseHistoryService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
