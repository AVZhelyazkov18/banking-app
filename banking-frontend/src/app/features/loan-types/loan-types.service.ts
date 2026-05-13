import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoanTypeDTO } from '../../models/loan-type.model';

@Injectable({ providedIn: 'root' })
export class LoanTypesService {
  private apiUrl = 'http://localhost:8080/api/loan_types';

  constructor(private http: HttpClient) {}

  getLoanTypes(): Observable<LoanTypeDTO[]> {
    return this.http.get<LoanTypeDTO[]>(this.apiUrl);
  }

  getLoanType(id: number): Observable<LoanTypeDTO> {
    return this.http.get<LoanTypeDTO>(`${this.apiUrl}/${id}`);
  }

  createLoanType(dto: LoanTypeDTO): Observable<LoanTypeDTO> {
    return this.http.post<LoanTypeDTO>(this.apiUrl, dto);
  }

  updateLoanType(id: number, dto: LoanTypeDTO): Observable<LoanTypeDTO> {
    return this.http.put<LoanTypeDTO>(`${this.apiUrl}/${id}`, dto);
  }

  deleteLoanType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
