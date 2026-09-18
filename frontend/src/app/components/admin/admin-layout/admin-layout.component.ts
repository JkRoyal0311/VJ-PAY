import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AdminAuthService } from '../../../services/admin-auth.service';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './admin-layout.component.html'
})
export class AdminLayoutComponent {
  sidebarOpen = false;

  navItems = [
    { label: 'Overview', route: '/admin', icon: 'grid', exact: true },
    { label: 'Customers', route: '/admin/customers', icon: 'users', exact: false },
    // { label: 'Wallets', route: '/admin/wallets', icon: 'wallet', exact: false },
    // { label: 'Bank Accounts', route: '/admin/bank-accounts', icon: 'bank', exact: false },
    // { label: 'Beneficiaries', route: '/admin/beneficiaries', icon: 'people', exact: false },
    { label: 'Bill Payments', route: '/admin/bill-payments', icon: 'receipt', exact: false },
    { label: 'Transactions', route: '/admin/transactions', icon: 'chart', exact: false },
  ];

  constructor(private adminAuth: AdminAuthService, private router: Router) {}

  logout() {
    this.adminAuth.logout();
    this.router.navigate(['/admin-login']);
  }
}
