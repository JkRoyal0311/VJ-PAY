import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AdminCustomerService } from '../../../services/admin-customer.service';
import { AdminWalletService } from '../../../services/admin-wallet.service';
import { AdminBankAccountService } from '../../../services/admin-bank-account.service';
import { AdminTransactionService } from '../../../services/admin-transaction.service';

@Component({
  selector: 'app-admin-overview',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-overview.component.html'
})
export class AdminOverviewComponent implements OnInit {
  stats = { customers: 0, wallets: 0, bankAccounts: 0, transactions: 0 };
  loading = true;
  error = '';

  constructor(
    private customerSvc: AdminCustomerService,
    private walletSvc: AdminWalletService,
    private bankSvc: AdminBankAccountService,
    private txSvc: AdminTransactionService
  ) {}

  ngOnInit() {
    forkJoin({
      customers: this.customerSvc.getAll(),
      wallets: this.walletSvc.getAll(),
      bankAccounts: this.bankSvc.getAll(),
      transactions: this.txSvc.getAll()
    }).subscribe({
      next: (data) => {
        this.stats.customers = data.customers.length;
        this.stats.wallets = data.wallets.length;
        this.stats.bankAccounts = data.bankAccounts.length;
        this.stats.transactions = data.transactions.length;
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load stats.'; this.loading = false; }
    });
  }
}
