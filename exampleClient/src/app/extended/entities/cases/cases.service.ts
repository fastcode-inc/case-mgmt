import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CasesService } from 'src/app/entities/cases/cases.service';
@Injectable({
  providedIn: 'root',
})
export class CasesExtendedService extends CasesService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
