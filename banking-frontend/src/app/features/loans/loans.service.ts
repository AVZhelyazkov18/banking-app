import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoanDTO, CreateLoanDTO } from '../../models/loan.model';

@Injectable({ providedIn: 'root' })
export class LoansService {
  private apiUrl = 'http://localhost:8080/api/loans';

  constructor(private http: HttpClient) {}

  getLoans(): Observable<LoanDTO[]> {
    return this.http.get<LoanDTO[]>(this.apiUrl);
  }

  getLoan(id: number): Observable<LoanDTO> {
    return this.http.get<LoanDTO>(`${this.apiUrl}/${id}`);
  }

  createLoan(dto: CreateLoanDTO): Observable<LoanDTO> {
    return this.http.post<LoanDTO>(this.apiUrl, dto);
  }

  updateLoan(id: number, dto: CreateLoanDTO): Observable<LoanDTO> {
    return this.http.put<LoanDTO>(`${this.apiUrl}/${id}`, dto);
  }

  deleteLoan(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getMyLoans(): Observable<LoanDTO[]> {
    return this.http.get<LoanDTO[]>(`${this.apiUrl}/my`);
  }

  payNextInstallment(loanId: number, bankAccountId: number): Observable<LoanDTO> {
    return this.http.post<LoanDTO>(`${this.apiUrl}/${loanId}/pay`, {
      bankAccountId
    });
  }
}
