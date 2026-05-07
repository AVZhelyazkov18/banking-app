import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  form: FormGroup;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      role: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.authService.register(this.form.value).subscribe({
      next: () => this.router.navigate(['/login']),
      error: err => this.errorMessage = err.error?.message || 'Registration failed'
    });
  }
}
