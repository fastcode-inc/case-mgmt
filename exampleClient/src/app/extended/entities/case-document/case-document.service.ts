import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CaseDocumentService } from 'src/app/entities/case-document/case-document.service';
@Injectable({
  providedIn: 'root',
})
export class CaseDocumentExtendedService extends CaseDocumentService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
