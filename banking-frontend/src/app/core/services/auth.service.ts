import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse, UserProfileResponse, UpdateProfileRequest } from '../../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  private email: string | null = null;
  private role: string | null = null;

  constructor(private http: HttpClient, private router: Router) {
    this.email = localStorage.getItem('auth_email');
    this.role = localStorage.getItem('auth_role');
  }

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, req, { withCredentials: true }).pipe(
      tap(response => {
        this.email = response.email;
        this.role = response.role;
        localStorage.setItem('auth_email', response.email);
        localStorage.setItem('auth_role', response.role);
      })
    );
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, req, { withCredentials: true }).pipe(
      tap(response => {
        this.email = response.email;
        this.role = response.role;
        localStorage.setItem('auth_email', response.email);
        localStorage.setItem('auth_role', response.role);
      })
    );
  }

  logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).subscribe({
      complete: () => {
        this.email = null;
        this.role = null;
        localStorage.removeItem('auth_email');
        localStorage.removeItem('auth_role');
        this.router.navigate(['/login']);
      }
    });
  }

  me(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/me`, { withCredentials: true }).pipe(
      tap(response => {
        this.email = response.email;
        this.role = response.role;
        localStorage.setItem('auth_email', response.email);
        localStorage.setItem('auth_role', response.role);
      })
    );
  }

  isLoggedIn(): boolean {
    return this.email !== null;
  }

  getRole(): string | null {
    return this.role;
  }

  getEmail(): string | null {
    return this.email;
  }

  getProfile(): Observable<UserProfileResponse> {
    return this.http.get<UserProfileResponse>(`${this.apiUrl}/profile`, { withCredentials: true });
  }

  updateProfile(req: UpdateProfileRequest): Observable<UserProfileResponse> {
    return this.http.post<UserProfileResponse>(`${this.apiUrl}/profile`, req, { withCredentials: true });
  }
}
