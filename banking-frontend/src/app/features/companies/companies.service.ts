import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompanyDTO, UpdateCompanyDTO } from '../../models/company.model';

@Injectable({ providedIn: 'root' })
export class CompaniesService {
  private apiUrl = 'http://localhost:8080/api/companies';

  constructor(private http: HttpClient) {}

  getCompanies(): Observable<CompanyDTO[]> {
    return this.http.get<CompanyDTO[]>(this.apiUrl);
  }

  getCompany(id: number): Observable<CompanyDTO> {
    return this.http.get<CompanyDTO>(`${this.apiUrl}/${id}`);
  }

  createCompany(dto: CompanyDTO): Observable<CompanyDTO> {
    return this.http.post<CompanyDTO>(this.apiUrl, dto);
  }

  updateCompany(id: number, dto: UpdateCompanyDTO): Observable<CompanyDTO> {
    return this.http.put<CompanyDTO>(`${this.apiUrl}/${id}`, dto);
  }

  deleteCompany(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
