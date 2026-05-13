import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentPlanDTO } from '../../models/payment-plan.model';

@Injectable({ providedIn: 'root' })
export class PaymentPlansService {
  private apiUrl = 'http://localhost:8080/api/payment_plans';

  constructor(private http: HttpClient) {}

  getPaymentPlans(): Observable<PaymentPlanDTO[]> {
    return this.http.get<PaymentPlanDTO[]>(this.apiUrl);
  }

  getPaymentPlan(id: number): Observable<PaymentPlanDTO> {
    return this.http.get<PaymentPlanDTO>(`${this.apiUrl}/${id}`);
  }

  createPaymentPlan(dto: PaymentPlanDTO): Observable<PaymentPlanDTO> {
    return this.http.post<PaymentPlanDTO>(this.apiUrl, dto);
  }

  updatePaymentPlan(id: number, dto: PaymentPlanDTO): Observable<PaymentPlanDTO> {
    return this.http.put<PaymentPlanDTO>(`${this.apiUrl}/${id}`, dto);
  }

  deletePaymentPlan(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
