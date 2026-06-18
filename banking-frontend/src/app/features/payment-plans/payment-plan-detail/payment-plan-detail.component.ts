import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentPlansService } from '../payment-plans.service';
import { PaymentPlanDTO } from '../../../models/payment-plan.model';

@Component({
  selector: 'app-payment-plan-detail',
  templateUrl: './payment-plan-detail.component.html'
})
export class PaymentPlanDetailComponent implements OnInit {
  paymentPlans: PaymentPlanDTO[] = [];
  loanId: number = 0;
  errorMessage = '';
  generated = false;
  loading = false;

  constructor(
    private paymentPlansService: PaymentPlansService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loanId = +this.route.snapshot.paramMap.get('id')!;
  }

  generate(): void {
    this.loading = true;
    this.errorMessage = '';
    this.paymentPlansService.getPaymentPlanFromLoan(this.loanId).subscribe({
      next: data => {
        this.paymentPlans = data;
        this.generated = true;
        this.loading = false;
      },
      error: err => {
        this.errorMessage = err.error?.message || 'Failed to generate payment plan';
        this.loading = false;
      }
    });
  }

  loadPlans(): void {
    this.paymentPlansService.getPaymentPlanFromLoan(this.loanId).subscribe({
      next: data => this.paymentPlans = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load payment plans'
    });
  }

  markAsPaid(id: number): void {
    this.paymentPlansService.markInstallmentAsPaid(id).subscribe({
      next: () => this.loadPlans(),
      error: err => this.errorMessage = err.error?.message || 'Payment failed'
    });
  }

  back(): void {
    this.router.navigate(['/loans']);
  }
}
