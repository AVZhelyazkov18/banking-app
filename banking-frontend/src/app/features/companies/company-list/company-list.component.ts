import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CompaniesService } from '../companies.service';
import { CompanyDTO } from '../../../models/company.model';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html'
})
export class CompanyListComponent implements OnInit {
  companies: CompanyDTO[] = [];
  errorMessage = '';

  constructor(private companiesService: CompaniesService, private router: Router) {}

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.companiesService.getCompanies().subscribe({
      next: data => this.companies = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load companies'
    });
  }

  // NOTE: The backend CompanyDTO does not return an id field.
  // Edit and Delete require an id — using index as temporary key.
  // These actions will be fully functional once the backend returns id in responses.
  delete(id: number): void {
    this.companiesService.deleteCompany(id).subscribe({
      next: () => this.loadCompanies(),
      error: err => this.errorMessage = err.error?.message || 'Delete failed'
    });
  }

  edit(id: number): void {
    this.router.navigate(['/companies', id, 'edit']);
  }

  create(): void {
    this.router.navigate(['/companies/new']);
  }
}
