import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoansService } from '../loans.service';
import { LoanTypesService } from '../../loan-types/loan-types.service';
import { CustomersService } from '../../customers/customers.service';
import { LoanTypeDTO } from '../../../models/loan-type.model';
import { CustomerOptionDTO } from '../../../models/customer-option.model';
import { CreateLoanDTO } from '../../../models/loan.model';

@Component({
  selector: 'app-loan-form',
  templateUrl: './loan-form.component.html'
})
export class LoanFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  loanId: number = 0;
  loanTypes: LoanTypeDTO[] = [];
  customers: CustomerOptionDTO[] = [];
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private loansService: LoansService,
    private loanTypesService: LoanTypesService,
    private customersService: CustomersService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      customerId: [null, [Validators.required, Validators.min(1)]],
      amountDisbursed: [null, [Validators.required, Validators.min(1)]],
      paymentTerm: [1, [Validators.required, Validators.min(1)]],
      loanTypeId: [null, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadLoanTypes();
    this.loadCustomers();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEditMode = true;
      this.loanId = +id;

      this.loansService.getLoan(this.loanId).subscribe({
        next: data => this.form.patchValue({
          customerId: data.customerId ?? null,
          amountDisbursed: data.amountDisbursed,
          paymentTerm: data.paymentTerm,
          loanTypeId: data.loanType.id
        }),
        error: (err: any) => this.errorMessage = err.error?.message || 'Failed to load loan'
      });
    }
  }

  loadLoanTypes(): void {
    this.loanTypesService.getLoanTypes().subscribe({
      next: (data: LoanTypeDTO[]) => this.loanTypes = data.filter(loanType => !!loanType.id),
      error: (err: any) => this.errorMessage = err.error?.message || 'Failed to load loan types'
    });
  }

  loadCustomers(): void {
    this.customersService.getCustomerOptions().subscribe({
      next: (data: CustomerOptionDTO[]) => this.customers = data.filter(customer => !!customer.id),
      error: (err: any) => this.errorMessage = err.error?.message || 'Failed to load customers'
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { customerId, amountDisbursed, paymentTerm, loanTypeId } = this.form.value;

    const payload: CreateLoanDTO = {
      customerId: Number(customerId),
      amountDisbursed: Number(amountDisbursed),
      paymentTerm: Number(paymentTerm),
      loanType: {
        id: Number(loanTypeId)
      }
    };

    if (this.isEditMode) {
      this.loansService.updateLoan(this.loanId, payload).subscribe({
        next: () => this.router.navigate(['/loans']),
        error: (err: any) => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      this.loansService.createLoan(payload).subscribe({
        next: () => this.router.navigate(['/loans']),
        error: (err: any) => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/loans']);
  }
}
