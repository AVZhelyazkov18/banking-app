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

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    PeopleListComponent,
    PersonFormComponent,
    CompanyListComponent,
    CompanyFormComponent
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
