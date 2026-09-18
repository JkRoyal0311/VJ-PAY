import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BeneficiaryService } from '../../services/beneficiary.service';
import { Beneficiary } from '../../models/beneficiary.model';
import { PaymentService } from '../../services/payment.service';

declare var Razorpay: any;

@Component({
  selector: 'app-beneficiary',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './beneficiary.component.html'
})
export class BeneficiaryComponent implements OnInit {
  beneficiaries: Beneficiary[] = [];
  loading = true;
  error = '';
  successMsg = '';
  showForm = false;
  sendModal: Beneficiary | null = null;
  sendAmount = 0;

  form = { beneficiaryName: '', mobileNumber: '' };

  constructor(private beneficiaryService: BeneficiaryService, private paymentService: PaymentService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.beneficiaryService.getAll().subscribe({
      next: (data) => { this.beneficiaries = data; this.loading = false; },
      error: () => { this.error = 'Failed to load beneficiaries.'; this.loading = false; }
    });
  }

  addBeneficiary() {
    if (!/^\d{10}$/.test(this.form.mobileNumber)) {
      this.error = 'Mobile number must be exactly 10 digits.';
      return;
    }
    this.beneficiaryService.add(this.form).subscribe({
      next: (msg) => {
        this.successMsg = "Beneficiary Added";
        this.showForm = false;
        this.form = { beneficiaryName: '', mobileNumber: '' };
        this.load();
      },
      error: () => { this.error = 'Failed to add beneficiary.'; }
    });
  }

  delete(id: number) {
    if (!confirm('Delete this beneficiary?')) return;
    this.beneficiaryService.delete(id).subscribe({
      next: (msg) => { this.successMsg = "Beneficiary Deleted"; this.load(); },
      error: () => { this.error = 'Failed to delete.'; }
    });
  }

  openSend(b: Beneficiary) {
    this.sendModal = b;
    this.sendAmount = 0;
  }

  doSend() {
    if (!this.sendModal || this.sendAmount <= 0) return;

    this.paymentService.createOrder({ amount: this.sendAmount, currency: 'INR' }).subscribe({
      next: (order) => {
        const options = {
          key: order.keyId,
          amount: order.amount.toString(),
          currency: order.currency,
          name: 'VJ Pay Wallet',
          description: 'Transfer to ' + this.sendModal?.beneficiaryName,
          order_id: order.orderId,
          handler: (response: any) => {
            this.paymentService.verifyPayment({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature
            }).subscribe({
              next: () => {
                this.executeTransfer();
              },
              error: () => { this.error = 'Payment verification failed.'; }
            });
          },
          prefill: {
            name: 'Test User',
            email: 'test@vjpay.com',
            contact: '9999999999'
          },
          theme: { color: '#1a0533' }
        };
        const rzp = new Razorpay(options);
        rzp.open();
      },
      error: () => { this.error = 'Failed to initiate payment gateway.'; }
    });
  }

  private executeTransfer() {
    if (!this.sendModal) return;
    this.beneficiaryService.sendMoney(this.sendModal.mobileNumber, this.sendAmount).subscribe({
      next: (msg) => { this.successMsg = "Transfer Successful!"; this.sendModal = null; this.load(); },
      error: (err) => { this.error = err.error?.message || 'Transfer failed.'; }
    });
  }
}
