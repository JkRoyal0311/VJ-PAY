import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminCustomerService } from '../../../services/admin-customer.service';
import { Customer } from '../../../models/customer.model';

@Component({
  selector: 'app-admin-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-customers.component.html'
})
export class AdminCustomersComponent implements OnInit {
  customers: Customer[] = [];
  loading = true;
  error = '';
  successMsg = '';
  searchQuery = '';

  editModal: Customer | null = null;
  editForm = { custName: '', mobileNumber: '', email: '' };

  constructor(private svc: AdminCustomerService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => { this.customers = data; this.loading = false; },
      error: () => { this.error = 'Failed to load customers.'; this.loading = false; }
    });
  }

  search() {
    if (!this.searchQuery.trim()) { this.load(); return; }
    this.loading = true;
    this.svc.search(this.searchQuery).subscribe({
      next: (data) => { this.customers = data; this.loading = false; },
      error: () => { this.error = 'Search failed.'; this.loading = false; }
    });
  }

  openEdit(c: Customer) {
    this.editModal = c;
    this.editForm = { custName: c.custName, mobileNumber: c.mobileNumber, email: c.email };
  }

  saveEdit() {
    if (!this.editModal) return;
    this.svc.update(this.editModal.custId, this.editForm).subscribe({
      next: () => { this.successMsg = 'Customer updated.'; this.editModal = null; this.load(); },
      error: () => { this.error = 'Update failed.'; }
    });
  }

  delete(id: number) {
    if (!confirm('Delete this customer? This cannot be undone.')) return;
    this.svc.delete(id).subscribe({
      next: (msg) => { this.successMsg = "Deleted Successfully"; this.load(); },
      error: () => { this.error = 'Delete failed.'; }
    });
  }
}
