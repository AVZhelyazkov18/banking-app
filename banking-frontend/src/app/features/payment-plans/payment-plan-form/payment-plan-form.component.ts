import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentPlansService } from '../payment-plans.service';

@Component({
  selector: 'app-payment-plan-form',
  templateUrl: './payment-plan-form.component.html'
})
export class PaymentPlanFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  planId: number = 0;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private paymentPlansService: PaymentPlansService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      contributionAmount: [0, [Validators.required, Validators.min(0)]],
      principalPortion: [0, [Validators.required, Validators.min(0)]],
      interestPortion: [0, [Validators.required, Validators.min(0)]],
      date: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.planId = +id;
      this.paymentPlansService.getPaymentPlan(this.planId).subscribe({
        next: data => this.form.patchValue(data),
        error: err => this.errorMessage = err.error?.message || 'Failed to load payment plan'
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    if (this.isEditMode) {
      this.paymentPlansService.updatePaymentPlan(this.planId, this.form.value).subscribe({
        next: () => this.router.navigate(['/payment-plans']),
        error: err => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      this.paymentPlansService.createPaymentPlan(this.form.value).subscribe({
        next: () => this.router.navigate(['/payment-plans']),
        error: err => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/payment-plans']);
  }
}
