export interface BankAccount {
  bankAccountId: number;
  accountNo: string;
  ifscCode: string;
  bankname: string;
  balance: number;
}

export interface AddBankAccountRequest {
  accountNo: string;
  ifscCode: string;
  bankname: string;
}
