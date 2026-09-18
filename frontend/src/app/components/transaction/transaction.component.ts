import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { Transaction, TransactionCategory, TransactionSubCategory } from '../../models/transaction.model';

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction.component.html'
})
export class TransactionComponent implements OnInit {
  transactions: Transaction[] = [];
  singleTransaction!: Transaction;
  showSingleTransaction=false;
  filtered: Transaction[] = [];
  loading = true;
  error = '';
  successMsg = '';

  filterCategory = '';
  filterSubCategory = '';
  filterFrom = '';
  filterTo = '';

  categories: TransactionCategory[] = ['BENEFICIARY_TRANSFER', 'BILL_PAYMENT', 'WALLET_TOP_UP'];
  subcategories: TransactionSubCategory[] = ['ELECTRICITY', 'MOBILE_RECHARGE', 'GAS'];

  constructor(private txService: TransactionService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.txService.getAll().subscribe({
      next: (data) => {
        this.transactions = data;
        this.filtered = data;
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load transactions.'; this.loading = false; }
    });
  }

  applyFilter() {
    if (this.filterCategory && this.filterFrom && this.filterTo) {
      this.txService.getByCategoryAndDate(this.filterCategory as TransactionCategory,this.filterFrom, this.filterTo).subscribe({
        next: (data) => { this.filtered = data; },
        error: () => { this.error = 'Filter failed.'; }
      });
    }else if (this.filterSubCategory && this.filterFrom && this.filterTo) {
      this.txService.getBySubCategoryAndDate(this.filterSubCategory as TransactionSubCategory,this.filterFrom, this.filterTo).subscribe({
        next: (data) => { this.filtered = data; },
        error: () => { this.error = 'Filter failed.'; }
      });
    } else if (this.filterCategory) {
      this.txService.getByCategory(this.filterCategory as TransactionCategory).subscribe({
        next: (data) => { this.filtered = data; },
        error: () => { this.error = 'Filter failed.'; }
      });
    } else if (this.filterSubCategory as TransactionSubCategory) {
      this.txService.getBySubCategory(this.filterSubCategory as TransactionSubCategory).subscribe({
        next: (data) => { this.filtered = data; },
        error: () => { this.error = 'Filter failed.'; }
      });
    } else if (this.filterFrom && this.filterTo) {
      this.txService.getByDateRange(this.filterFrom, this.filterTo).subscribe({
        next: (data) => { this.filtered = data; },
        error: () => { this.error = 'Filter failed.'; }
      });
    
    } else {
      this.filtered = this.transactions;
    }
  }

  clearFilter() {
    this.filterCategory = '';
    this.filterFrom = '';
    this.filterTo = '';
    this.filtered = this.transactions;
  }

  deleteTransaction(id: number) {
    if (!confirm('Delete this transaction?')) return;
    this.txService.delete(id).subscribe({
      next: (msg) => { this.successMsg = "Transaction Deleted"; this.load(); },
      error: () => { this.error = 'Failed to delete.'; }
    });
  }

  txTypeBadge(type: string): string {
    if (type === 'DEBIT') return 'bg-red-500/15 text-red-400 border border-red-500/20 text-xs font-medium px-2.5 py-1 rounded-full';
    if (type === 'CREDIT') return 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/20 text-xs font-medium px-2.5 py-1 rounded-full';
    return 'bg-slate-700 text-slate-300 text-xs font-medium px-2.5 py-1 rounded-full';
  }

  fetchSingleTransaction(transaction:Transaction){
    this.showSingleTransaction=true;
    this.singleTransaction=transaction;
  }
  closeSingleTransaction(){
    this.showSingleTransaction=false;
  }
}
