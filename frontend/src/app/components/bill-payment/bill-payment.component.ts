import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BillPaymentService } from '../../services/bill-payment.service';
import { BillPaymentResponse, BillType, MobileRechargeOperators } from '../../models/bill-payment.model';
import { PaymentService } from '../../services/payment.service';

declare var Razorpay: any;

@Component({
  selector: 'app-bill-payment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bill-payment.component.html'
})
export class BillPaymentComponent implements OnInit {
  bills: BillPaymentResponse[] = [];
  filtered: BillPaymentResponse[] = [];
  loading = true;
  error = '';
  successMsg = '';
  showForm = false;

  singleBillPayment!: BillPaymentResponse;
  showSingleBillPayment=false;
  singleBillPaymentBillData!:any;

  billTypes: BillType[] = ['ELECTRICITY', 'MOBILE_RECHARGE', 'GAS_BOOKING'];
  mobileRechargeOperators: MobileRechargeOperators[] = ['JIO','VI', 'AIRTEL', 'BSNL'];

  filterType= "";
  filterFrom = '';
  filterTo = '';

  form = {
    amount: 0,
    billType: 'ELECTRICITY' as BillType,
    billData: {}
  };

  mobileRechargeBillData = {
    mobileNumber: "",
    operator: 'JIO' as MobileRechargeOperators
  }

  gasCylinderBookingBillData = {
    gasProvider : "",
    customerNumber : ""
  }

  electricityBookingBillData = {
    state : "",
    billerName : "",
    accountNumber : "",
  }


  constructor(private billService: BillPaymentService, private paymentService: PaymentService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.billService.getAll().subscribe({
      next: (data) => { this.bills = data,this.filtered=data; this.loading = false; },
      error: () => { this.error = 'Failed to load bills.'; this.loading = false; }
    });
  }

  createBill() {
    const billData ={};
    if(this.form.billType=="ELECTRICITY"){
      console.log("e");
      console.log(this.electricityBookingBillData);
      this.form.billData=this.electricityBookingBillData;
    }
    else if(this.form.billType=="MOBILE_RECHARGE"){
      console.log("m");
      console.log(this.mobileRechargeBillData);
      this.form.billData=this.mobileRechargeBillData;
    }
    else if(this.form.billType=="GAS_BOOKING"){
      console.log("g");
      console.log(this.gasCylinderBookingBillData);
      this.form.billData=this.gasCylinderBookingBillData;
    }

    console.log(this.form)

    // if (this.form.billDataKey) billData[this.form.billDataKey] = this.form.billDataValue;

    // Initialize Razorpay
    this.paymentService.createOrder({ amount: this.form.amount, currency: 'INR' }).subscribe({
      next: (order) => {
        const options = {
          key: order.keyId, // YOUR_KEY_ID_HERE
          amount: order.amount.toString(),
          currency: order.currency,
          name: 'VJ Pay Wallet',
          description: 'Test Transaction for ' + this.form.billType,
          order_id: order.orderId,
          handler: (response: any) => {
            // Verify payment
            this.paymentService.verifyPayment({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature
            }).subscribe({
              next: (verifyRes) => {
                // Payment verified, now save the bill
                this.saveBillPayment();
              },
              error: () => { this.error = 'Payment verification failed.'; }
            });
          },
          prefill: {
            name: 'Test User',
            email: 'test@vjpay.com',
            contact: '9999999999'
          },
          theme: {
            color: '#1a0533' // App theme color
          }
        };
        const rzp = new Razorpay(options);
        rzp.open();
      },
      error: () => { this.error = 'Failed to initiate payment gateway.'; }
    });
  }

  private saveBillPayment() {
    this.billService.create({
      amount: this.form.amount,
      billType: this.form.billType,
      billData: this.form.billData
    }).subscribe({
      next: (res) => {
        this.successMsg = res.message || 'Payment Successful!';
        this.showForm = false;
        this.form = { amount: 0, billType: 'ELECTRICITY', billData: {} };
        this.load();
      },
      error: () => { this.error = 'Failed to create bill payment record.'; }
    });
  }

  deleteBill(id: number) {
    if (!confirm('Delete this bill?')) return;
    this.billService.delete(id).subscribe({
      next: (msg) => { this.successMsg = "Deleted Successfully"; this.load(); },
      error: () => { this.error = 'Failed to delete bill.'; }
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

  applyFilter() {
     if (this.filterType && this.filterFrom && this.filterTo) {
        this.billService.getBetweenByType(this.filterFrom,this.filterTo,this.filterType).subscribe({
          next: (data) => { this.filtered = data; },
          error: () => { this.error = 'Filter failed.'; }
        });
      } else if (this.filterType) {
        this.billService.getByType(this.filterType as BillType).subscribe({
          next: (data) => { this.filtered = data; },
          error: () => { this.error = 'Filter failed.'; }
        });
      } else if (this.filterFrom && this.filterTo) {
        this.billService.getBetween(this.filterFrom, this.filterTo).subscribe({
          next: (data) => { this.filtered = data; },
          error: () => { this.error = 'Filter failed.'; }
        });
      
      } else {
        this.filtered = this.bills;
      }
    }

    clearFilter() {
    this.filterType = '';
    this.filterFrom = '';
    this.filterTo = '';
    this.filtered = this.bills;
  }

  fetchSingleBillPayment(billPayment:BillPaymentResponse){
    this.showSingleBillPayment=true;
    this.singleBillPayment=billPayment;
    this.singleBillPaymentBillData=billPayment.billData;
  }
  
  closeSingleBillPayment(){
    this.showSingleBillPayment=false;
  }

  getColumns(data:any){
    return Object.keys(data);
  }

}
