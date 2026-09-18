import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminTransactionService } from '../../../services/admin-transaction.service';
import { Transaction, TransactionCategory, TransactionSubCategory } from '../../../models/transaction.model';

@Component({
  selector: 'app-admin-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-transactions.component.html'
})
export class AdminTransactionsComponent implements OnInit {
  transactions: Transaction[] = [];
  filtered: Transaction[] = [];
  loading = true;
  error = '';

  filterCategory = '';
  filterSubCategory = '';
  filterFrom = '';
  filterTo = '';
  filterMonth = '';
  filterYear = '';
  query="";

  categories: TransactionCategory[] = ['BENEFICIARY_TRANSFER', 'BILL_PAYMENT', 'WALLET_TOP_UP'];
  subcategories: TransactionSubCategory[] = ['ELECTRICITY', 'MOBILE_RECHARGE', 'GAS'];

  constructor(private svc: AdminTransactionService) { }

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => { this.transactions = data; this.filtered = data; this.loading = false; },
      error: () => { this.error = 'Failed to load transactions.'; this.loading = false; }
    });
  }

  applyFilter() {
    this.loading = true;
    if (this.filterCategory && this.filterFrom && this.filterTo) {
      this.svc.getByCategoryAndDate(this.filterCategory as TransactionCategory, this.filterFrom, this.filterTo).subscribe({
        next: (data) => { this.filtered = data; this.loading = false;},
        error: () => { this.error = 'Filter failed.'; this.loading = false;}
      });
    } else if (this.filterSubCategory && this.filterFrom && this.filterTo) {
      this.svc.getBySubCategoryAndDate(this.filterSubCategory as TransactionSubCategory, this.filterFrom, this.filterTo).subscribe({
        next: (data) => { this.filtered = data; this.loading = false;},
        error: () => { this.error = 'Filter failed.'; this.loading = false;}
      });
    }
    else if (this.filterCategory) {
      this.svc.getByCategory(this.filterCategory as TransactionCategory).subscribe({
        next: (data) => { this.filtered = data; this.loading = false; },
        error: () => { this.error = 'Filter failed.'; this.loading = false; }
      });
    } else if (this.filterSubCategory as TransactionSubCategory) {
      this.svc.getBySubCategory(this.filterSubCategory as TransactionSubCategory).subscribe({
        next: (data) => { this.filtered = data;this.loading = false; },
        error: () => { this.error = 'Filter failed.'; this.loading = false; }
      });
    } else if (this.filterFrom && this.filterTo) {
      this.svc.getByDateRange(this.filterFrom, this.filterTo).subscribe({
        next: (data) => { this.filtered = data; this.loading = false; },
        error: () => { this.error = 'Filter failed.'; this.loading = false; }
      });
    } else if (this.filterMonth && this.filterYear) {
      this.svc.getByMonth(parseInt(this.filterMonth), parseInt(this.filterYear)).subscribe({
        next: (data) => { this.filtered = data; this.loading = false; },
        error: () => { this.error = 'Filter failed.'; this.loading = false; }
      });
    } else {
      this.filtered = this.transactions;
      this.loading = false;
    }
  }

  search(){
    this.loading=true;
    this.error="";
    this.svc.search(this.query).subscribe({
      next: (data) => { this.filtered = data; this.loading = false;},
        error: () => { this.error = 'Search failed.';this.filtered = this.transactions; this.loading = false;}
    })
  }

  clearFilter() {
    this.filterCategory = '';
    this.filterFrom = '';
    this.filterTo = '';
    this.filterMonth = '';
    this.filterYear = '';
    this.filtered = this.transactions;
    this.query="";
  }

  txTypeBadge(type: string): string {
    if (type === 'DEBIT') return 'bg-red-500/15 text-red-400 border border-red-500/20 text-xs font-medium px-2.5 py-1 rounded-full';
    if (type === 'CREDIT') return 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/20 text-xs font-medium px-2.5 py-1 rounded-full';
    return 'bg-slate-700 text-slate-300 text-xs font-medium px-2.5 py-1 rounded-full';
  }
}
