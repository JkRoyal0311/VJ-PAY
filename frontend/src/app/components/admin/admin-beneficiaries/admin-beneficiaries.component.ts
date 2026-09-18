import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminBeneficiaryService } from '../../../services/admin-beneficiary.service';
import { Beneficiary } from '../../../models/beneficiary.model';

@Component({
  selector: 'app-admin-beneficiaries',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-beneficiaries.component.html'
})
export class AdminBeneficiariesComponent implements OnInit {
  beneficiaries: Beneficiary[] = [];
  loading = true;
  error = '';
  successMsg = '';
  filterCustomerId = '';

  constructor(private svc: AdminBeneficiaryService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => { this.beneficiaries = data; this.loading = false; },
      error: () => { this.error = 'Failed to load beneficiaries.'; this.loading = false; }
    });
  }

  filterByCustomer() {
    const id = parseInt(this.filterCustomerId);
    if (!id) { this.load(); return; }
    this.loading = true;
    this.svc.getByCustomer(id).subscribe({
      next: (data) => { this.beneficiaries = data; this.loading = false; },
      error: (err) => { this.error = 'Filter failed. '+err.error.message;console.log(err); this.loading = false; }
    });
  }

  delete(id: number) {
    if (!confirm('Delete this beneficiary?')) return;
    this.svc.delete(id).subscribe({
      next: (msg) => { this.successMsg = msg; this.load(); },
      error: () => { this.error = 'Delete failed.'; }
    });
  }
}
