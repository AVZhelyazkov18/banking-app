import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { UserProfileResponse } from '../../models/auth.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  profile: UserProfileResponse | null = null;
  form!: FormGroup;
  isEditing = false;
  successMessage = '';
  errorMessage = '';

  constructor(private authService: AuthService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (p) => {
        this.profile = p;
        this.form = this.fb.group({
          firstName: [p.firstName, [Validators.required, Validators.minLength(2)]],
          lastName: [p.lastName, [Validators.required, Validators.minLength(2)]],
          phone: [p.phone ?? ''],
          address: [p.address ?? '']
        });
      },
      error: () => this.errorMessage = 'Failed to load profile.'
    });
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    this.successMessage = '';
    this.errorMessage = '';
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.authService.updateProfile(this.form.value).subscribe({
      next: (updated) => {
        this.profile = updated;
        this.isEditing = false;
        this.successMessage = 'Profile updated successfully.';
      },
      error: () => this.errorMessage = 'Failed to update profile.'
    });
  }
}
