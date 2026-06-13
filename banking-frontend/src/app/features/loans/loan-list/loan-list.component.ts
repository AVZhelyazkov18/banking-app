import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoansService } from '../loans.service';
import { LoanDTO } from '../../../models/loan.model';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html'
})
export class LoanListComponent implements OnInit {
  loans: LoanDTO[] = [];
  errorMessage = '';

  constructor(private loansService: LoansService, private router: Router) {}

  ngOnInit(): void {
    this.loadLoans();
  }

  loadLoans(): void {
    this.loansService.getLoans().subscribe({
      next: data => this.loans = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load loans'
    });
  }

  create(): void {
    this.router.navigate(['/loans/new']);
  }

  edit(id: number): void {
    this.router.navigate(['/loans', id, 'edit']);
  }

  delete(id: number): void {
    this.loansService.deleteLoan(id).subscribe({
      next: () => this.loadLoans(),
      error: err => this.errorMessage = err.error?.message || 'Delete failed'
    });
  }
}
