import { Beneficiary } from './beneficiary.model';

export interface Wallet {
  walletId: number;
  balance: number;
  beneficiary: Beneficiary[];
}

export interface WalletBalance {
  balance: number;
}
