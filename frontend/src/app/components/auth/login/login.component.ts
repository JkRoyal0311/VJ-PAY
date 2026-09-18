import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  pwd = '';
  error = '';
  loading = false;
  showForgetPasswordForm = false;
  otpSent = false;
  otpLoading = false;
  otp = '';
  newPwd = '';

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    if (!this.email || !this.pwd) { this.error = 'Please fill in all fields.'; return; }
    this.loading = true; this.error = '';
    this.auth.login({ email: this.email, pwd: this.pwd }).subscribe({
      next: (res) => { this.auth.saveToken(res.token); this.router.navigate(['/dashboard']); },
      error: () => { this.error = 'Invalid credentials. Please try again.'; this.loading = false; }
    });
  }

  setShowForgetPasswordForm() { this.showForgetPasswordForm = !this.showForgetPasswordForm; }

  sendOtp() {
    this.otpLoading = true; this.error = '';
    this.auth.sendotp({ email: this.email }).subscribe((data) => {
      this.otpSent = true; localStorage.setItem('otp', data.otp); this.otpLoading = false;
    });
  }

  forgetPassword() {
    if (!this.otp || this.otp?.length < 6) { this.error = 'Please enter valid otp.'; return; }
    const otpToken = localStorage.getItem('otp');
    if (!otpToken) { this.error = 'Something went wrong'; return; }
    this.loading = true;
    this.auth.forgetPassword({ email: this.email, otpToken, otp: this.otp, newPwd: this.newPwd }).subscribe({
      next: (res) => { localStorage.removeItem('otp'); this.loading = false; this.error = ''; this.auth.saveToken(res.token); this.router.navigate(['/dashboard']); },
      error: () => { this.loading = false; this.error = 'Invalid OTP'; }
    });
  }
}
