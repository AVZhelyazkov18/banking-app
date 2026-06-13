import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PeopleService } from '../people.service';

@Component({
  selector: 'app-person-form',
  templateUrl: './person-form.component.html'
})
export class PersonFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  personId: number = 0;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private peopleService: PeopleService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      pin: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.personId = +id;
      this.form.get('pin')!.disable();
      this.peopleService.getPerson(this.personId).subscribe({
        next: data => this.form.patchValue(data),
        error: err => this.errorMessage = err.error?.message || 'Failed to load person'
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    if (this.isEditMode) {
      const { firstName, lastName } = this.form.getRawValue();
      this.peopleService.updatePerson(this.personId, { firstName, lastName }).subscribe({
        next: () => this.router.navigate(['/people']),
        error: err => this.errorMessage = err.error?.message || 'Update failed'
      });
    } else {
      this.peopleService.createPerson(this.form.value).subscribe({
        next: () => this.router.navigate(['/people']),
        error: err => this.errorMessage = err.error?.message || 'Create failed'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/people']);
  }
}
