import { Component, OnInit } from '@angular/core';
import { UserListResponse } from '../../models/user-management.model';
import { UserManagementService } from './user-management.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit {
  users: UserListResponse[] = [];
  errorMessage = '';
  successMessage = '';
  readonly roles = ['ROLE_USER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN'];

  constructor(private userManagementService: UserManagementService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.userManagementService.getAllUsers().subscribe({
      next: users => this.users = users,
      error: err => this.errorMessage = err.error?.message || 'Failed to load users'
    });
  }

  changeRole(user: UserListResponse, role: string): void {
    this.successMessage = '';
    this.errorMessage = '';
    this.userManagementService.updateRole(user.id, role).subscribe({
      next: updated => {
        user.role = updated.role;
        this.successMessage = `Role updated for ${user.email}`;
      },
      error: err => this.errorMessage = err.error?.message || 'Failed to update role'
    });
  }

  roleBadgeClass(role: string): string {
    if (role === 'ROLE_ADMIN') return 'badge-inactive';
    if (role === 'ROLE_EMPLOYEE') return 'badge-active';
    return 'badge-neutral';
  }
}
