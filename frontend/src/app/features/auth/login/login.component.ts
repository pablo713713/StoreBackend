import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  hidePassword = true;
  errorMessage = '';

  loggedClient: any = null;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  onSubmit() {
    this.http.post<Client>('http://localhost:8080/clients/login',
      { email: this.email, password: this.password }
    ).subscribe({
      next: client => { this.loggedClient = client; this.router.navigate(['/']); },
      error: _ => this.errorMessage = 'Credenciales inválidas'
    });
  }

  goToAdminLogin() {
    this.router.navigate(['/admin-login']);
  }
}
