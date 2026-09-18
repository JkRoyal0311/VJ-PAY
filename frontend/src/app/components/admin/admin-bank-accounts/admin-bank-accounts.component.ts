import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminBankAccountService } from '../../../services/admin-bank-account.service';
import { BankAccount } from '../../../models/bank-account.model';

@Component({
  selector: 'app-admin-bank-accounts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-bank-accounts.component.html'
})
export class AdminBankAccountsComponent implements OnInit {
  accounts: BankAccount[] = [];
  loading = true;
  error = '';
  successMsg = '';
  searchQuery = '';
  showForm = false;

  form = { accountNo: '', ifscCode: '', bankname: '', balance: 0, custId: 0 };

  constructor(private svc: AdminBankAccountService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => { this.accounts = data; this.loading = false; },
      error: () => { this.error = 'Failed to load accounts.'; this.loading = false; }
    });
  }

  search() {
    if (!this.searchQuery.trim()) { this.load(); return; }
    this.loading = true;
    this.svc.search(this.searchQuery).subscribe({
      next: (data) => { this.accounts = data; this.loading = false; },
      error: () => { this.error = 'Search failed.'; this.loading = false; }
    });
  }

  add() {
    this.svc.add(this.form).subscribe({
      next: () => {
        this.successMsg = 'Bank account added.';
        this.showForm = false;
        this.form = { accountNo: '', ifscCode: '', bankname: '', balance: 0, custId: 0 };
        this.load();
      },
      error: () => { this.error = 'Failed to add account.'; }
    });
  }

  delete(id: number) {
    if (!confirm('Delete this bank account?')) return;
    this.svc.delete(id).subscribe({
      next: (msg) => { this.successMsg = msg; this.load(); },
      error: () => { this.error = 'Delete failed.'; }
    });
  }
}
