import { Component, OnInit } from '@angular/core';
import { LoansService } from '../loans/loans.service';
import { LoanDTO } from '../../models/loan.model';

@Component({
  selector: 'app-my-loans',
  templateUrl: './my-loans.component.html'
})
export class MyLoansComponent implements OnInit {
  loans: LoanDTO[] = [];
  errorMessage = '';

  constructor(private loansService: LoansService) {}

  ngOnInit(): void {
    this.loansService.getMyLoans().subscribe({
      next: data => this.loans = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load your loans'
    });
  }
}
