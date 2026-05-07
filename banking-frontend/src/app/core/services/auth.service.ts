import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse } from '../../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, req).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('role', response.role);
      })
    );
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, req);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }
}
