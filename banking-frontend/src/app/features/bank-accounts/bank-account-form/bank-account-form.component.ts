import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BankAccountsService } from '../bank-accounts.service';

@Component({
  selector: 'app-bank-account-form',
  templateUrl: './bank-account-form.component.html'
})
export class BankAccountFormComponent implements OnInit {
  form: FormGroup;
  accountId: number = 0;
  iban: string = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private bankAccountsService: BankAccountsService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      balance: [0, [Validators.required, Validators.min(0)]],
      status: [true, Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.accountId = +id;
      this.bankAccountsService.getBankAccount(this.accountId).subscribe({
        next: data => {
          this.iban = data.IBAN;
          this.form.patchValue({ balance: data.balance, status: data.status });
        },
        error: err => this.errorMessage = err.error?.message || 'Failed to load bank account'
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    const { balance, status } = this.form.value;
    this.bankAccountsService.updateBankAccount(this.accountId, { balance, status }).subscribe({
      next: () => this.router.navigate(['/bank-accounts']),
      error: err => this.errorMessage = err.error?.message || 'Update failed'
    });
  }

  cancel(): void {
    this.router.navigate(['/bank-accounts']);
  }
}
