import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface PaymentOrderRequest {
  amount: number;
  currency: string;
}

export interface PaymentOrderResponse {
  orderId: string;
  amount: number;
  currency: string;
  keyId: string;
}

export interface PaymentVerifyRequest {
  razorpayOrderId: string;
  razorpayPaymentId: string;
  razorpaySignature: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = `${environment.apiUrl}/api/payment`;

  constructor(private http: HttpClient) {}

  createOrder(request: PaymentOrderRequest): Observable<PaymentOrderResponse> {
    return this.http.post<PaymentOrderResponse>(`${this.apiUrl}/create-order`, request);
  }

  verifyPayment(request: PaymentVerifyRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/verify`, request);
  }
}
