import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PersonDTO, UpdatePersonDTO } from '../../models/person.model';

@Injectable({ providedIn: 'root' })
export class PeopleService {
  private apiUrl = 'http://localhost:8080/api/people';

  constructor(private http: HttpClient) {}

  getPeople(): Observable<PersonDTO[]> {
    return this.http.get<PersonDTO[]>(this.apiUrl);
  }

  getPerson(id: number): Observable<PersonDTO> {
    return this.http.get<PersonDTO>(`${this.apiUrl}/${id}`);
  }

  createPerson(dto: PersonDTO): Observable<PersonDTO> {
    return this.http.post<PersonDTO>(this.apiUrl, dto);
  }

  updatePerson(id: number, dto: UpdatePersonDTO): Observable<PersonDTO> {
    return this.http.put<PersonDTO>(`${this.apiUrl}/${id}`, dto);
  }

  deletePerson(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
