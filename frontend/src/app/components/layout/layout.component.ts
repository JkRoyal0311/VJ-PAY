import { Component, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';
import { TransactionService } from '../../services/transaction.service';

interface NavItem {
  label: string;
  route: string;
  emoji: string;
  bgColor: string;
}

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.component.html'
})
export class LayoutComponent implements OnInit {
  sidebarOpen = false;
  customer: Customer | null = null;
  
  // Toast state
  toastVisible = false;
  toastData: any = null;
  toastTimeout: any;

  navItems: NavItem[] = [
    { label: 'Dashboard', route: '/dashboard', emoji: '🏠', bgColor: '#f0eeff' },
    { label: 'Wallet', route: '/wallet', emoji: '💰', bgColor: '#e8f8f5' },
    { label: 'Bank Accounts', route: '/bank-account', emoji: '🏦', bgColor: '#eef3ff' },
    { label: 'Beneficiaries', route: '/beneficiary', emoji: '👥', bgColor: '#faf0ff' },
    { label: 'Bill Payments', route: '/bill-payment', emoji: '📋', bgColor: '#fff8e1' },
    { label: 'Transactions', route: '/transactions', emoji: '📊', bgColor: '#e8f8f5' },
  ];

  constructor(
    private auth: AuthService,
    private router: Router,
    private customerService: CustomerService,
    private txService: TransactionService
  ) { }

  ngOnInit() {
    this.customerService.getDetails().subscribe({
      next: (data) => {
        this.customer = data;
      },
      error: () => {
        console.error('Failed to load customer details in layout');
      }
    });
  }

  showLastAction() {
    this.txService.getAll().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          const lastTx = data[data.length - 1];
          const action = lastTx.transactionType === 'CREDIT' ? 'Received' : 'Paid/Sent';
          this.showToast({
            title: 'Last Action',
            message: `${action} ₹${lastTx.transactionAmount}`,
            category: lastTx.category ? lastTx.category.replace(/_/g, ' ') : 'GENERAL',
            date: new Date(lastTx.transactionDate).toLocaleDateString(),
            icon: '🔔'
          });
        } else {
          this.showToast({ title: 'No Activity', message: 'No recent actions found.', icon: 'ℹ️' });
        }
      },
      error: () => {
        this.showToast({ title: 'Error', message: 'Could not fetch the last action.', icon: '❌' });
      }
    });
  }

  showToast(data: any) {
    this.toastData = data;
    this.toastVisible = true;
    if (this.toastTimeout) clearTimeout(this.toastTimeout);
    this.toastTimeout = setTimeout(() => {
      this.toastVisible = false;
    }, 4000);
  }

  closeToast() {
    this.toastVisible = false;
    if (this.toastTimeout) clearTimeout(this.toastTimeout);
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
