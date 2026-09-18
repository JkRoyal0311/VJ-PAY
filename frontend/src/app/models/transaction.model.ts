export type TransactionCategory = 'BENEFICIARY_TRANSFER' | 'BILL_PAYMENT' | 'WALLET_TOP_UP';
// export type TransactionSubCategory = 'ELECTRICITY' | 'MOBILE_RECHARGE' | 'GAS' | 'WATER' | 'BROADBAND' | 'NONE';
export type TransactionSubCategory = 'ELECTRICITY' | 'MOBILE_RECHARGE' | 'GAS';

export interface Transaction {
  transactionId: number;
  transactionType: string;
  transactionStatus: string;
  transactionAmount: number;
  customerId: number;
  category: TransactionCategory;
  subCategory: TransactionSubCategory;
  description: string;
  transactionDate: string;
}
