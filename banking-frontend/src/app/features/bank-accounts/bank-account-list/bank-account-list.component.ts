import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BankAccountsService } from '../bank-accounts.service';
import { BankAccountDTO } from '../../../models/bank-account.model';

@Component({
  selector: 'app-bank-account-list',
  templateUrl: './bank-account-list.component.html'
})
export class BankAccountListComponent implements OnInit {
  bankAccounts: BankAccountDTO[] = [];
  errorMessage = '';

  constructor(private bankAccountsService: BankAccountsService, private router: Router) {}

  ngOnInit(): void {
    this.loadBankAccounts();
  }

  loadBankAccounts(): void {
    this.bankAccountsService.getBankAccounts().subscribe({
      next: data => this.bankAccounts = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load bank accounts'
    });
  }

  create(): void {
    this.router.navigate(['/bank-accounts/new']);
  }

  edit(id: number): void {
    this.router.navigate(['/bank-accounts', id, 'edit']);
  }

  delete(id: number): void {
    this.bankAccountsService.deleteBankAccount(id).subscribe({
      next: () => this.loadBankAccounts(),
      error: err => this.errorMessage = err.error?.message || 'Delete failed'
    });
  }
}
