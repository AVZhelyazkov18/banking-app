import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { PeopleListComponent } from './features/people/people-list/people-list.component';
import { PersonFormComponent } from './features/people/person-form/person-form.component';
import { CompanyListComponent } from './features/companies/company-list/company-list.component';
import { CompanyFormComponent } from './features/companies/company-form/company-form.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'people', component: PeopleListComponent, canActivate: [AuthGuard] },
  { path: 'people/new', component: PersonFormComponent, canActivate: [AuthGuard] },
  { path: 'people/:id/edit', component: PersonFormComponent, canActivate: [AuthGuard] },
  { path: 'companies', component: CompanyListComponent, canActivate: [AuthGuard] },
  { path: 'companies/new', component: CompanyFormComponent, canActivate: [AuthGuard] },
  { path: 'companies/:id/edit', component: CompanyFormComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/people', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
