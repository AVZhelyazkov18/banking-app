import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';

import { NavbarComponent } from './shared/components/navbar/navbar.component';
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
import { PaymentPlanDetailComponent } from './features/payment-plans/payment-plan-detail/payment-plan-detail.component';
import { HomeComponent } from './features/home/home.component';
import { ProfileComponent } from './features/profile/profile.component';
import { UserManagementComponent } from './features/user-management/user-management.component';
import { MyLoansComponent } from './features/my-loans/my-loans.component';
import { MyAccountsComponent } from './features/my-accounts/my-accounts.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    PeopleListComponent,
    PersonFormComponent,
    CompanyListComponent,
    CompanyFormComponent,
    BankAccountListComponent,
    BankAccountFormComponent,
    LoanTypeListComponent,
    LoanTypeFormComponent,
    LoanListComponent,
    LoanFormComponent,
    PaymentPlanListComponent,
    PaymentPlanFormComponent,
    PaymentPlanDetailComponent,
    ProfileComponent,
    UserManagementComponent,
    MyLoansComponent,
    MyAccountsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
