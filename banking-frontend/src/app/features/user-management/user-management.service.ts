import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserListResponse } from '../../models/user-management.model';

@Injectable({ providedIn: 'root' })
export class UserManagementService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserListResponse[]> {
    return this.http.get<UserListResponse[]>(this.apiUrl, { withCredentials: true });
  }

  updateRole(id: number, role: string): Observable<UserListResponse> {
    return this.http.put<UserListResponse>(`${this.apiUrl}/${id}/role`, { role }, { withCredentials: true });
  }
}
