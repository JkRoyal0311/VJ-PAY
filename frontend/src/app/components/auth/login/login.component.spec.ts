import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../services/auth.service';
import { AuthResponse, OtpResponse } from '../../../models/customer.model';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', [
      'login',
      'saveToken',
      'sendotp',
      'forgetPassword'
    ]);

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call auth.login on onSubmit with valid credentials', () => {
    const mockResponse: AuthResponse = { token: 'mock-token-123', email: 'user@example.com' };
    authServiceSpy.login.and.returnValue(of(mockResponse));

    component.email = 'user@example.com';
    component.pwd = 'secret123';
    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith({
      email: 'user@example.com',
      pwd: 'secret123'
    });
    expect(component.loading).toBeTrue();
  });

  it('should navigate to /dashboard on successful login', () => {
    const mockResponse: AuthResponse = { token: 'mock-token-123', email: 'user@example.com' };
    authServiceSpy.login.and.returnValue(of(mockResponse));

    component.email = 'user@example.com';
    component.pwd = 'secret123';
    component.onSubmit();

    expect(authServiceSpy.saveToken).toHaveBeenCalledWith('mock-token-123');
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should toggle forgot password form', () => {
    expect(component.showForgetPasswordForm).toBeFalse();
    component.setShowForgetPasswordForm();
    expect(component.showForgetPasswordForm).toBeTrue();
    component.setShowForgetPasswordForm();
    expect(component.showForgetPasswordForm).toBeFalse();
  });

  it('should call auth.sendotp on sendOtp', () => {
    const mockOtpResponse: OtpResponse = { otp: '654321', message: 'OTP sent' };
    authServiceSpy.sendotp.and.returnValue(of(mockOtpResponse));

    component.email = 'user@example.com';
    component.sendOtp();

    expect(authServiceSpy.sendotp).toHaveBeenCalledWith({ email: 'user@example.com' });
    expect(component.otpSent).toBeTrue();
    expect(localStorage.getItem('otp')).toBe('654321');
    expect(component.otpLoading).toBeFalse();
  });

  it('should show error when submitting empty credentials', () => {
    component.email = '';
    component.pwd = '';
    component.onSubmit();

    expect(component.error).toBe('Please fill in all fields.');
    expect(authServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should handle login failure', () => {
    authServiceSpy.login.and.returnValue(throwError(() => new Error('Unauthorized')));

    component.email = 'user@example.com';
    component.pwd = 'wrongpassword';
    component.onSubmit();

    expect(component.error).toBe('Invalid credentials. Please try again.');
    expect(component.loading).toBeFalse();
  });
});
