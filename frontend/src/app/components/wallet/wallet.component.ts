import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WalletService } from '../../services/wallet.service';
import { Wallet } from '../../models/wallet.model';
import { BankAccount } from '../../models/bank-account.model';
import { BankAccountService } from '../../services/bank-account.service';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';

declare var Razorpay: any;

@Component({
  selector: 'app-wallet',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './wallet.component.html'
})
export class WalletComponent implements OnInit {
  wallet: Wallet | null = null;
  loading = true;
  error = '';
  showTopUpAccount=false;

  accounts: BankAccount[] = [];
  transferModal: BankAccount | null = null;
  transferAmount = 0;
  bankloading=false;
  bankError=""
  successMsg="";
  
  constructor(private walletService: WalletService,private bankService: BankAccountService, private paymentService: PaymentService) {}
  
  ngOnInit() {
    this.load();
  }

  load(){
    this.loading=true;
    this.walletService.getWallet().subscribe({
      next: (data) => { this.wallet = data; this.loading = false; },
      error: () => { this.error = 'Failed to load wallet.'; this.loading = false; }
    });
  }

  openTransfer(account: BankAccount) {
    this.transferModal = account;
    this.transferAmount = 0;
  }

  showBankAccounts(){
    this.showTopUpAccount = true;
    this.loadBanks();
  }

  loadBanks(){
    this.bankloading = true;
    this.bankService.getAll().subscribe({
      next: (data) => { this.accounts = data; this.bankloading = false; },
      error: () => { this.bankError = 'Failed to load accounts.'; this.bankloading = false; }
    });
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
          description: 'Top Up from ' + this.transferModal?.bankname,
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
              error: () => { this.bankError = 'Payment verification failed.'; }
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
      error: () => { this.bankError = 'Failed to initiate payment gateway.'; }
    });
  }

  private executeTransfer() {
    if (!this.transferModal) return;
    this.bankService.transferToWallet(this.transferModal.bankAccountId, this.transferAmount).subscribe({
      next: (msg) => {
        this.successMsg = "Transfer Complete";
        this.transferModal = null;
        this.load();
        this.loadBanks();
      },
      error: () => { this.bankError = 'Transfer failed. Insufficent Balance';this.transferModal = null;this.loadBanks();this.load(); }
    });
  }
}
