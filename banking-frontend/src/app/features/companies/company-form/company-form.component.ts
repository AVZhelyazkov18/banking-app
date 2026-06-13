import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CompaniesService } from '../companies.service';

@Component({
  selector: 'app-company-form',
  templateUrl: './company-form.component.html'
})
export class CompanyFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  companyId: number = 0;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private companiesService: CompaniesService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      companyName: ['', [Validators.required, Validators.minLength(2)]],
      eik: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      representative: ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.companyId = +id;
      this.form.get('eik')!.disable();
      this.companiesService.getCompany(this.companyId).subscribe({
        next: data => this.form.patchValue(data),
        error: err => this.errorMessage = err.error?.message || 'Failed to load company'
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    if (this.isEditMode) {
      const { companyName, representative } = this.form.getRawValue();
      this.companiesService.updateCompany(this.companyId, { companyName, representative }).subscribe({
        next: () => this.router.navigate(['/companies']),
        error: err => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      this.companiesService.createCompany(this.form.value).subscribe({
        next: () => this.router.navigate(['/companies']),
        error: err => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/companies']);
  }
}
