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
  isEditMode = false;
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
      customerId: [null],
      balance: [0, [Validators.required, Validators.min(0)]],
      status: [true, Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.accountId = +id;
      this.bankAccountsService.getBankAccount(this.accountId).subscribe({
        next: data => {
          this.iban = data.IBAN;
          this.form.patchValue({ balance: data.balance, status: data.status });
        },
        error: err => this.errorMessage = err.error?.message || 'Failed to load bank account'
      });
    } else {
      this.form.get('customerId')!.setValidators([Validators.required, Validators.min(1)]);
      this.form.get('customerId')!.updateValueAndValidity();
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    if (this.isEditMode) {
      const { balance, status } = this.form.value;
      this.bankAccountsService.updateBankAccount(this.accountId, { balance, status }).subscribe({
        next: () => this.router.navigate(['/bank-accounts']),
        error: err => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      const { customerId, balance } = this.form.value;
      this.bankAccountsService.createBankAccount({ customerId, balance }).subscribe({
        next: () => this.router.navigate(['/bank-accounts']),
        error: err => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/bank-accounts']);
  }
}
