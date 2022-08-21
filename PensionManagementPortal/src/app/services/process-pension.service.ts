import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PensionDetail } from '../models/pension-detail';
import { PensionerInput } from '../models/pensioner-input';

@Injectable({
  providedIn: 'root'
})
export class ProcessPensionService {

  // setting the base URL here
  baseUrl: string = 'http://localhost:8082/api/process-pension'
  //baseUrl: string = 'http://abhishek-service-lb-839332951.us-east-1.elb.amazonaws.com/api/process-pension'

  constructor(private http: HttpClient) { }

  // Method to get pension detail
  getPensionDetails(pensionerInput: PensionerInput): Observable<PensionDetail> {
    return this.http.post<PensionDetail>(`${this.baseUrl}/ProcessPension`, pensionerInput);
  }

}