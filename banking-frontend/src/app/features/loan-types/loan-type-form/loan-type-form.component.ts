import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoanTypesService } from '../loan-types.service';

@Component({
  selector: 'app-loan-type-form',
  templateUrl: './loan-type-form.component.html'
})
export class LoanTypeFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  loanTypeId: number = 0;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private loanTypesService: LoanTypesService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      creditName: ['', [Validators.required, Validators.minLength(2)]],
      creditDisbursedMin: [0, [Validators.required, Validators.min(0)]],
      creditDisbursedMax: [0, [Validators.required, Validators.min(0)]],
      creditTermMin: [1, [Validators.required, Validators.min(1)]],
      creditTermMax: [1, [Validators.required, Validators.min(1)]],
      creditInterestRate: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loanTypeId = +id;
      this.loanTypesService.getLoanType(this.loanTypeId).subscribe({
        next: data => this.form.patchValue(data),
        error: err => this.errorMessage = err.error?.message || 'Failed to load loan type'
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    if (this.isEditMode) {
      this.loanTypesService.updateLoanType(this.loanTypeId, this.form.value).subscribe({
        next: () => this.router.navigate(['/loan-types']),
        error: err => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      this.loanTypesService.createLoanType(this.form.value).subscribe({
        next: () => this.router.navigate(['/loan-types']),
        error: err => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/loan-types']);
  }
}
