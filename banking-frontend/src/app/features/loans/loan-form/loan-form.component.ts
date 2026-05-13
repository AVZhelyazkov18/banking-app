import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoansService } from '../loans.service';
import { LoanTypesService } from '../../loan-types/loan-types.service';
import { LoanTypeDTO } from '../../../models/loan-type.model';

@Component({
  selector: 'app-loan-form',
  templateUrl: './loan-form.component.html'
})
export class LoanFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  loanId: number = 0;
  loanTypes: LoanTypeDTO[] = [];
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private loansService: LoansService,
    private loanTypesService: LoanTypesService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      amountDisbursed: [0, [Validators.required, Validators.min(0)]],
      paymentTerm: [1, [Validators.required, Validators.min(1)]],
      loanTypeId: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loanTypesService.getLoanTypes().subscribe({
      next: data => this.loanTypes = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load loan types'
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loanId = +id;
      this.loansService.getLoan(this.loanId).subscribe({
        next: data => this.form.patchValue({
          amountDisbursed: data.amountDisbursed,
          paymentTerm: data.paymentTerm,
          loanTypeId: data.loanType.id
        }),
        error: err => this.errorMessage = err.error?.message || 'Failed to load loan'
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    const { amountDisbursed, paymentTerm, loanTypeId } = this.form.value;
    const payload = { amountDisbursed, paymentTerm, loanType: { id: loanTypeId } };
    if (this.isEditMode) {
      this.loansService.updateLoan(this.loanId, payload).subscribe({
        next: () => this.router.navigate(['/loans']),
        error: err => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      this.loansService.createLoan(payload).subscribe({
        next: () => this.router.navigate(['/loans']),
        error: err => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/loans']);
  }
}
