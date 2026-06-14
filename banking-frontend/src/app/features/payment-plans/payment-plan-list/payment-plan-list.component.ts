import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentPlansService } from '../payment-plans.service';
import { PaymentPlanDTO } from '../../../models/payment-plan.model';

@Component({
  selector: 'app-payment-plan-list',
  templateUrl: './payment-plan-list.component.html'
})
export class PaymentPlanListComponent implements OnInit {
  paymentPlans: PaymentPlanDTO[] = [];
  errorMessage = '';

  constructor(private paymentPlansService: PaymentPlansService, private router: Router) {}

  ngOnInit(): void {
    this.loadPaymentPlans();
  }

  loadPaymentPlans(): void {
    this.paymentPlansService.getPaymentPlans().subscribe({
      next: data => this.paymentPlans = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load payment plans'
    });
  }

  create(): void {
    this.router.navigate(['/payment-plans/new']);
  }

  edit(id: number): void {
    this.router.navigate(['/payment-plans', id, 'edit']);
  }

  delete(id: number): void {
    this.paymentPlansService.deletePaymentPlan(id).subscribe({
      next: () => this.loadPaymentPlans(),
      error: err => this.errorMessage = err.error?.message || 'Delete failed'
    });
  }

  markAsPaid(id: number): void {
    this.paymentPlansService.markInstallmentAsPaid(id).subscribe({
      next: () => this.loadPaymentPlans(),
      error: err => this.errorMessage = err.error?.message || 'Payment failed'
    });
  }
}
