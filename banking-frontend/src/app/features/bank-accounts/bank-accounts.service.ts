import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankAccountDTO, CreateBankAccountDTO, UpdateBankAccountDTO } from '../../models/bank-account.model';

@Injectable({ providedIn: 'root' })
export class BankAccountsService {
  private apiUrl = 'http://localhost:8080/api/bank-accounts';

  constructor(private http: HttpClient) {}

  getBankAccounts(): Observable<BankAccountDTO[]> {
    return this.http.get<BankAccountDTO[]>(this.apiUrl);
  }

  getBankAccount(id: number): Observable<BankAccountDTO> {
    return this.http.get<BankAccountDTO>(`${this.apiUrl}/${id}`);
  }

  createBankAccount(dto: CreateBankAccountDTO): Observable<BankAccountDTO> {
    return this.http.post<BankAccountDTO>(this.apiUrl, dto);
  }

  updateBankAccount(id: number, dto: UpdateBankAccountDTO): Observable<BankAccountDTO> {
    return this.http.put<BankAccountDTO>(`${this.apiUrl}/${id}`, dto);
  }

  deleteBankAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getMyBankAccounts(): Observable<BankAccountDTO[]> {
    return this.http.get<BankAccountDTO[]>(`${this.apiUrl}/my`);
  }
}
