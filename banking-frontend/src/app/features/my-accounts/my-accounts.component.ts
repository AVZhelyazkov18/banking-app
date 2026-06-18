import { Component, OnInit } from '@angular/core';
import { BankAccountsService } from '../bank-accounts/bank-accounts.service';
import { BankAccountDTO } from '../../models/bank-account.model';

@Component({
  selector: 'app-my-accounts',
  templateUrl: './my-accounts.component.html'
})
export class MyAccountsComponent implements OnInit {
  accounts: BankAccountDTO[] = [];
  errorMessage = '';

  constructor(private bankAccountsService: BankAccountsService) {}

  ngOnInit(): void {
    this.bankAccountsService.getMyBankAccounts().subscribe({
      next: data => this.accounts = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load your accounts'
    });
  }
}
