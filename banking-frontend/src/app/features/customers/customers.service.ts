import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerOptionDTO } from '../../models/customer-option.model';

@Injectable({ providedIn: 'root' })
export class CustomersService {
  private apiUrl = 'http://localhost:8080/api/customers';

  constructor(private http: HttpClient) {}

  getCustomerOptions(): Observable<CustomerOptionDTO[]> {
    return this.http.get<CustomerOptionDTO[]>(`${this.apiUrl}/options`);
  }
}
