import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminWalletService } from '../../../services/admin-wallet.service';
import { Wallet } from '../../../models/wallet.model';

@Component({
  selector: 'app-admin-wallets',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-wallets.component.html'
})
export class AdminWalletsComponent implements OnInit {
  wallets: Wallet[] = [];
  loading = true;
  error = '';
  successMsg = '';

  actionModal: { wallet: Wallet; type: 'credit' | 'debit' } | null = null;
  actionAmount = 0;

  constructor(private svc: AdminWalletService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => { this.wallets = data; this.loading = false; },
      error: () => { this.error = 'Failed to load wallets.'; this.loading = false; }
    });
  }

  openAction(wallet: Wallet, type: 'credit' | 'debit') {
    this.actionModal = { wallet, type };
    this.actionAmount = 0;
  }

  doAction() {
    if (!this.actionModal) return;
    const { wallet, type } = this.actionModal;
    const call = type === 'credit'
      ? this.svc.credit(wallet.walletId, this.actionAmount)
      : this.svc.debit(wallet.walletId, this.actionAmount);

    call.subscribe({
      next: () => {
        this.successMsg = `Wallet ${type === 'credit' ? 'credited' : 'debited'} successfully.`;
        this.actionModal = null;
        this.load();
      },
      error: () => { this.error = `${type} failed.`; }
    });
  }
}
