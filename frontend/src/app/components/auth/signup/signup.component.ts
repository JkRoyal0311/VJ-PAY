import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signup.component.html'
})
export class SignupComponent {
  custName = '';
  showOtpForm = false;
  otp = '';
  mobileNumber = '';
  email = '';
  pwd = '';
  error = '';
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    if (!this.custName || !this.mobileNumber || !this.email || !this.pwd) { this.error = 'All fields are required.'; return; }
    if (!/^\d{10}$/.test(this.mobileNumber)) { this.error = 'Mobile number must be exactly 10 digits.'; return; }
    this.loading = true; this.error = '';
    this.auth.sendotp({ email: this.email }).subscribe({
      next: (data) => {
        this.showOtpForm = true; localStorage.setItem('otp', data.otp); this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to send OTP. Please try again.';
        this.loading = false;
      }
    });
  }

  signup() {
    if (!this.otp || this.otp?.length < 6) { this.error = 'Please enter valid otp.'; return; }
    const otpToken = localStorage.getItem('otp');
    if (!otpToken) { this.error = 'Something went wrong'; return; }
    this.loading = true;
    this.auth.verifyotp({ email: this.email, otpToken, otp: this.otp }).subscribe({
      next: () => {
        localStorage.removeItem('otp'); this.loading = false; this.error = '';
        this.auth.signup({ custName: this.custName, mobileNumber: this.mobileNumber, email: this.email, pwd: this.pwd }).subscribe({
          next: (res) => { this.auth.saveToken(res.token); this.router.navigate(['/dashboard']); },
          error: () => { this.error = 'Signup failed. Email may already be in use.'; this.loading = false; }
        });
      },
      error: () => { this.error = 'Invalid OTP'; this.loading = false; }
    });
  }
}
