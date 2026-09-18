export interface Beneficiary {
  beneficiaryId: number;
  beneficiaryName: string;
  mobileNumber: string;
}

export interface AddBeneficiaryRequest {
  beneficiaryName: string;
  mobileNumber: string;
}
