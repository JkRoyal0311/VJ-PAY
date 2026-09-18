import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccount } from '../../models/bank-account.model';
import { PaymentService } from '../../services/payment.service';

declare var Razorpay: any;

@Component({
  selector: 'app-bank-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bank-account.component.html'
})
export class BankAccountComponent implements OnInit {
  accounts: BankAccount[] = [];
  loading = true;
  error = '';
  successMsg = '';
  showForm = false;
  transferModal: BankAccount | null = null;
  transferAmount = 0;

  form = { accountNo: '', ifscCode: '', bankname: ''};

  constructor(private bankService: BankAccountService, private paymentService: PaymentService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.bankService.getAll().subscribe({
      next: (data) => { this.accounts = data; this.loading = false; },
      error: () => { this.error = 'Failed to load accounts.'; this.loading = false; }
    });
  }

  addAccount() {
    this.bankService.add(this.form).subscribe({
      next: () => {
        this.successMsg = 'Account added successfully.';
        this.showForm = false;
        this.form = { accountNo: '', ifscCode: '', bankname: ''};
        this.load();
      },
      error: () => { this.error = 'Failed to add account. Please Enter Valid Information'; }
    });
  }

  deleteAccount(id: number) {
    if (!confirm('Delete this bank account?')) return;
    this.bankService.delete(id).subscribe({
      next: () => { this.successMsg = 'Account deleted.'; this.load(); },
      error: () => { this.error = 'Failed to delete account.'; }
    });
  }

  openTransfer(account: BankAccount) {
    this.transferModal = account;
    this.transferAmount = 0;
  }

  doTransfer() {
    if (!this.transferModal || this.transferAmount <= 0) return;

    this.paymentService.createOrder({ amount: this.transferAmount, currency: 'INR' }).subscribe({
      next: (order) => {
        const options = {
          key: order.keyId,
          amount: order.amount.toString(),
          currency: order.currency,
          name: 'VJ Pay Wallet',
          description: 'Transfer to Wallet from ' + this.transferModal?.bankname,
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
    if (!this.transferModal) return;
    this.bankService.transferToWallet(this.transferModal.bankAccountId, this.transferAmount).subscribe({
      next: (msg) => {
        this.successMsg = "Transfer Complete";
        this.transferModal = null;
        this.load();
      },
      error: () => { this.error = 'Transfer failed.'; }
    });
  }
}
