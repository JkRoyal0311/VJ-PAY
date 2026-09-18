import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AdminAuthService } from '../../../services/admin-auth.service';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-login.component.html'
})
export class AdminLoginComponent {
  email = '';
  pwd = '';
  error = '';
  loading = false;

  constructor(
    private auth: AuthService,
    private adminAuth: AdminAuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (!this.email || !this.pwd) {
      this.error = 'Please fill in all fields.';
      return;
    }
    this.loading = true;
    this.error = '';
    // Admin logs in via the same endpoint — the backend validates the ADMIN role
    this.auth.loginAdmin({ email: this.email, pwd: this.pwd }).subscribe({
      next: (res) => {
        this.adminAuth.saveToken(res.token);
        this.router.navigate(['/admin']);
      },
      error: () => {
        this.error = 'Invalid credentials or insufficient permissions.';
        this.loading = false;
      }
    });
  }
}
