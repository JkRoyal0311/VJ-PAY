import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import {
  AuthResponse,
  EmailOtpRequest,
  ForgetPasswordRequest,
  LoginRequest,
  OtpRequest,
  OtpResponse,
  SignupRequest
} from '../models/customer.model';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call POST /customers/login on login()', () => {
    const mockRequest: LoginRequest = { email: 'user@example.com', pwd: 'password123' };
    const mockResponse: AuthResponse = { token: 'jwt-login-token', email: 'user@example.com' };

    service.login(mockRequest).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/customers/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
  });

  it('should call POST /customers/signup on signup()', () => {
    const mockRequest: SignupRequest = {
      custName: 'John Doe',
      mobileNumber: '9876543210',
      email: 'john@example.com',
      pwd: 'password123'
    };
    const mockResponse: AuthResponse = { token: 'jwt-signup-token', email: 'john@example.com' };

    service.signup(mockRequest).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/customers/signup`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
  });

  it('should save and get token from localStorage', () => {
    service.saveToken('jwt-sample-token');

    expect(service.getToken()).toBe('jwt-sample-token');
    expect(localStorage.getItem('token')).toBe('jwt-sample-token');
    expect(service.isLoggedIn()).toBeTrue();

    service.logout();
    expect(service.getToken()).toBeNull();
    expect(service.isLoggedIn()).toBeFalse();
  });

  it('should call POST /customers/send-otp on sendotp()', () => {
    const mockRequest: OtpRequest = { email: 'user@example.com' };
    const mockResponse: OtpResponse = { otp: '456789', message: 'OTP sent' };

    service.sendotp(mockRequest).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/customers/send-otp`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
  });

  it('should return authorization headers with token', () => {
    service.saveToken('bearer-token-123');
    const headers = service.getAuthHeaders();
    expect(headers).toEqual({ Authorization: 'Bearer bearer-token-123' });
  });

  it('should call POST /customers/verify-otp on verifyotp()', () => {
    const mockRequest: EmailOtpRequest = {
      email: 'user@example.com',
      otpToken: 'otp-token-value',
      otp: '123456'
    };
    const mockResponse: OtpResponse = { otp: '123456', message: 'OTP verified' };

    service.verifyotp(mockRequest).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/customers/verify-otp`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
  });

  it('should call POST /customers/forget-password on forgetPassword()', () => {
    const mockRequest: ForgetPasswordRequest = {
      email: 'user@example.com',
      otpToken: 'otp-token-value',
      otp: '123456',
      newPwd: 'brandNewPassword'
    };
    const mockResponse: OtpResponse = { otp: '123456', message: 'Password reset successful' };

    service.forgetPassword(mockRequest).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/customers/forget-password`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
  });
});
