import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { PeopleListComponent } from './features/people/people-list/people-list.component';
import { PersonFormComponent } from './features/people/person-form/person-form.component';
import { CompanyListComponent } from './features/companies/company-list/company-list.component';
import { CompanyFormComponent } from './features/companies/company-form/company-form.component';
import { BankAccountListComponent } from './features/bank-accounts/bank-account-list/bank-account-list.component';
import { BankAccountFormComponent } from './features/bank-accounts/bank-account-form/bank-account-form.component';
import { LoanTypeListComponent } from './features/loan-types/loan-type-list/loan-type-list.component';
import { LoanTypeFormComponent } from './features/loan-types/loan-type-form/loan-type-form.component';
import { LoanListComponent } from './features/loans/loan-list/loan-list.component';
import { LoanFormComponent } from './features/loans/loan-form/loan-form.component';
import { PaymentPlanListComponent } from './features/payment-plans/payment-plan-list/payment-plan-list.component';
import { PaymentPlanFormComponent } from './features/payment-plans/payment-plan-form/payment-plan-form.component';
import { HomeComponent } from './features/home/home.component';
import { ProfileComponent } from './features/profile/profile.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'people', component: PeopleListComponent, canActivate: [AuthGuard] },
  { path: 'people/new', component: PersonFormComponent, canActivate: [AuthGuard] },
  { path: 'people/:id/edit', component: PersonFormComponent, canActivate: [AuthGuard] },
  { path: 'companies', component: CompanyListComponent, canActivate: [AuthGuard] },
  { path: 'companies/new', component: CompanyFormComponent, canActivate: [AuthGuard] },
  { path: 'companies/:id/edit', component: CompanyFormComponent, canActivate: [AuthGuard] },
  { path: 'bank-accounts', component: BankAccountListComponent, canActivate: [AuthGuard] },
  { path: 'bank-accounts/:id/edit', component: BankAccountFormComponent, canActivate: [AuthGuard] },
  { path: 'loan-types', component: LoanTypeListComponent, canActivate: [AuthGuard] },
  { path: 'loan-types/new', component: LoanTypeFormComponent, canActivate: [AuthGuard] },
  { path: 'loan-types/:id/edit', component: LoanTypeFormComponent, canActivate: [AuthGuard] },
  { path: 'loans', component: LoanListComponent, canActivate: [AuthGuard] },
  { path: 'loans/new', component: LoanFormComponent, canActivate: [AuthGuard] },
  { path: 'loans/:id/edit', component: LoanFormComponent, canActivate: [AuthGuard] },
  { path: 'payment-plans', component: PaymentPlanListComponent, canActivate: [AuthGuard] },
  { path: 'payment-plans/new', component: PaymentPlanFormComponent, canActivate: [AuthGuard] },
  { path: 'payment-plans/:id/edit', component: PaymentPlanFormComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
