export type BillType = 'ELECTRICITY' | 'MOBILE_RECHARGE' | 'GAS_BOOKING';
export type MobileRechargeOperators = 'JIO' | 'VI' | 'AIRTEL' | 'BSNL';

export interface BillPaymentRequest {
  amount: number;
  billType: BillType;
  billData: {};
}

export interface BillPaymentResponse {
  billId: number;
  paymentDate: string;
  amount: number;
  billType: BillType;
  billData: {};
}

export interface BillPaymentResult {
  status: string;
  message: string;
  timestamp: number;
}
