import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AdminAuthService {

  // Admin uses the same JWT token stored under a separate key
  saveToken(token: string): void {
    localStorage.setItem('admin_token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('admin_token');
  }

  logout(): void {
    localStorage.removeItem('admin_token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getAuthHeaders() {
    return { Authorization: `Bearer ${this.getToken()}` };
  }
}
