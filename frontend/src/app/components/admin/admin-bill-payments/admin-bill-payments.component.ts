import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminBillPaymentService } from '../../../services/admin-bill-payment.service';
import { BillPaymentResponse, BillType } from '../../../models/bill-payment.model';

@Component({
  selector: 'app-admin-bill-payments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-bill-payments.component.html'
})
export class AdminBillPaymentsComponent implements OnInit {
  bills: BillPaymentResponse[] = [];
  loading = true;
  error = '';
  successMsg = '';

  filterType = '';
  filterStart = '';
  filterEnd = '';
  billTypes: BillType[] = ['ELECTRICITY', 'MOBILE_RECHARGE', 'GAS_BOOKING'];
  query="";

  constructor(private svc: AdminBillPaymentService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => { this.bills = data; this.loading = false; },
      error: () => { this.error = 'Failed to load bills.'; this.loading = false; }
    });
  }

  applyFilter() {
    this.loading = true;
    if (this.filterType) {
      this.svc.getByType(this.filterType).subscribe({
        next: (data) => { this.bills = data; this.loading = false; },
        error: () => { this.error = 'Filter failed.'; this.loading = false; }
      });
    } else if (this.filterStart && this.filterEnd) {
      this.svc.getBetween(this.filterStart, this.filterEnd).subscribe({
        next: (data) => { this.bills = data; this.loading = false; },
        error: () => { this.error = 'Filter failed.'; this.loading = false; }
      });
    } else {
      this.load();
    }
  }

  search(){
    this.loading=true;
    this.error="";
    this.svc.search(this.query).subscribe({
      next: (data) => { this.bills = data; this.loading = false; },
      error: () => { this.error = 'Failed to load bills.'; this.loading = false; }
    })
  }

  clearFilter() {
    this.filterType = '';
    this.filterStart = '';
    this.filterEnd = '';
    this.query="";
    this.load();
  }

  delete(id: number) {
    if (!confirm('Delete this bill payment?')) return;
    this.svc.delete(id).subscribe({
      next: (msg) => { this.successMsg = msg; this.load(); },
      error: () => { this.error = 'Delete failed.'; }
    });
  }

  billTypeBadge(type: string): string {
    const map: Record<string, string> = {
      ELECTRICITY: 'bg-amber-500/15 text-amber-400 border border-amber-500/20 text-xs font-medium px-2.5 py-1 rounded-full',
      MOBILE_RECHARGE: 'bg-blue-500/15 text-blue-400 border border-blue-500/20 text-xs font-medium px-2.5 py-1 rounded-full',
      GAS_BOOKING: 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/20 text-xs font-medium px-2.5 py-1 rounded-full',
    };
    return map[type] ?? 'bg-slate-700 text-slate-300 text-xs font-medium px-2.5 py-1 rounded-full';
  }
}
