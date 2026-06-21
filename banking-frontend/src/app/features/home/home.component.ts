import { Component, OnInit } from '@angular/core';
import { BankAccountsService } from '../bank-accounts/bank-accounts.service';
import { LoansService } from '../loans/loans.service';
import { BankAccountDTO } from '../../models/bank-account.model';
import { LoanDTO } from '../../models/loan.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  bankAccounts: BankAccountDTO[] = [];
  loans: LoanDTO[] = [];
  currentAccountIndex = 0;
  errorMessage = '';

  constructor(
    private bankAccountsService: BankAccountsService,
    private loansService: LoansService
  ) {}

  ngOnInit(): void {
    this.loadHomeData();
  }

  loadHomeData(): void {
    this.bankAccountsService.getMyBankAccounts().subscribe({
      next: data => this.bankAccounts = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load bank accounts'
    });

    this.loansService.getMyLoans().subscribe({
      next: data => this.loans = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load loans'
    });
  }

  get currentAccount(): BankAccountDTO | null {
    return this.bankAccounts.length > 0 ? this.bankAccounts[this.currentAccountIndex] : null;
  }

  prev(): void {
    if (this.currentAccountIndex > 0) {
      this.currentAccountIndex--;
    }
  }

  next(): void {
    if (this.currentAccountIndex < this.bankAccounts.length - 1) {
      this.currentAccountIndex++;
    }
  }

  payLoan(loan: LoanDTO): void {
    if (!this.currentAccount || !this.currentAccount.id) {
      this.errorMessage = 'Please select a bank account first';
      return;
    }

    if (!this.currentAccount.status) {
      this.errorMessage = 'Selected bank account is not active';
      return;
    }

    this.loansService.payNextInstallment(loan.id, this.currentAccount.id).subscribe({
      next: () => {
        this.errorMessage = '';
        this.loadHomeData();
      },
      error: err => this.errorMessage = err.error?.message || 'Payment failed'
    });
  }
}
