import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ICaseDocument } from './icase-document';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class FileService extends GenericApiService<ICaseDocument> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'docapi/file');
  }
}
