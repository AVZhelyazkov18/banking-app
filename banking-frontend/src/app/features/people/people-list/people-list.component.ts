import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PeopleService } from '../people.service';
import { PersonDTO } from '../../../models/person.model';

@Component({
  selector: 'app-people-list',
  templateUrl: './people-list.component.html'
})
export class PeopleListComponent implements OnInit {
  people: PersonDTO[] = [];
  errorMessage = '';

  constructor(private peopleService: PeopleService, private router: Router) {}

  ngOnInit(): void {
    this.loadPeople();
  }

  loadPeople(): void {
    this.peopleService.getPeople().subscribe({
      next: data => this.people = data,
      error: err => this.errorMessage = err.error?.message || 'Failed to load people'
    });
  }

  // NOTE: The backend PersonDTO does not return an id field.
  // Edit and Delete require an id — using index as temporary key.
  // These actions will be fully functional once the backend returns id in responses.
  delete(id: number): void {
    console.log(id)
    this.peopleService.deletePerson(id).subscribe({
      next: () => this.loadPeople(),
      error: err => this.errorMessage = err.error?.message || 'Delete failed'
    });
  }

  edit(id: number): void {
    console.log(id)
    this.router.navigate(['/people', id, 'edit']);
  }

  create(): void {
    this.router.navigate(['/people/new']);
  }
}
