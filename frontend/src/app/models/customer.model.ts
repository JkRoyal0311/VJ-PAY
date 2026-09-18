import { Wallet } from './wallet.model';

export interface Customer {
  custId: number;
  custName: string;
  mobileNumber: string;
  email: string;
  wallet: Wallet;
}

export interface SignupRequest {
  custName: string;
  mobileNumber: string;
  email: string;
  pwd: string;
}

export interface LoginRequest {
  email: string;
  pwd: string;
}

export interface AuthResponse {
  token: string;
  email: string;
}


export interface OtpRequest {
  email: string;
}

export interface OtpResponse {
  otp: string;
  message: string;
}

export interface EmailOtpRequest {
  email: string;
  otpToken: string;
  otp: string;
}

export interface ForgetPasswordRequest {
  email: string;
  otpToken: string;
  otp: string;
  newPwd:string;
}

export interface PasswordResetRequest {
  pwd: string;
  newPwd: string;
}