import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoanTypesService } from '../loan-types.service';
import { LoanTypeDTO } from '../../../models/loan-type.model';

@Component({
  selector: 'app-loan-type-list',
  templateUrl: './loan-type-list.component.html'
})
export class LoanTypeListComponent implements OnInit {
  loanTypes: LoanTypeDTO[] = [];
  errorMessage = '';

  constructor(private loanTypesService: LoanTypesService, private router: Router) {}

  ngOnInit(): void {
    this.loadLoanTypes();
  }

  loadLoanTypes(): void {
    this.loanTypesService.getLoanTypes().subscribe({
      next: data => this.loanTypes = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load loan types'
    });
  }

  create(): void {
    this.router.navigate(['/loan-types/new']);
  }

  edit(id: number): void {
    this.router.navigate(['/loan-types', id, 'edit']);
  }

  delete(id: number): void {
    this.loanTypesService.deleteLoanType(id).subscribe({
      next: () => this.loadLoanTypes(),
      error: err => this.errorMessage = err.error?.message || 'Delete failed'
    });
  }
}
