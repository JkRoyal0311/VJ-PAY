import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SignupComponent } from './signup.component';
import { AuthService } from '../../../services/auth.service';
import { AuthResponse, OtpResponse } from '../../../models/customer.model';

describe('SignupComponent', () => {
  let component: SignupComponent;
  let fixture: ComponentFixture<SignupComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', [
      'sendotp',
      'verifyotp',
      'signup',
      'saveToken'
    ]);

    await TestBed.configureTestingModule({
      imports: [SignupComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SignupComponent);
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

  it('should call sendotp on onSubmit', () => {
    const mockOtpResponse: OtpResponse = { otp: '654321', message: 'OTP Sent' };
    authServiceSpy.sendotp.and.returnValue(of(mockOtpResponse));

    component.custName = 'John Doe';
    component.mobileNumber = '9876543210';
    component.email = 'john@example.com';
    component.pwd = 'password123';

    component.onSubmit();

    expect(authServiceSpy.sendotp).toHaveBeenCalledWith({ email: 'john@example.com' });
    expect(component.showOtpForm).toBeTrue();
    expect(localStorage.getItem('otp')).toBe('654321');
    expect(component.loading).toBeFalse();
  });

  it('should validate form fields on onSubmit', () => {
    component.custName = '';
    component.mobileNumber = '';
    component.email = '';
    component.pwd = '';

    component.onSubmit();

    expect(component.error).toBe('All fields are required.');
    expect(authServiceSpy.sendotp).not.toHaveBeenCalled();
  });

  it('should validate 10-digit mobile number on onSubmit', () => {
    component.custName = 'John Doe';
    component.mobileNumber = '12345';
    component.email = 'john@example.com';
    component.pwd = 'password123';

    component.onSubmit();

    expect(component.error).toBe('Mobile number must be exactly 10 digits.');
    expect(authServiceSpy.sendotp).not.toHaveBeenCalled();
  });

  it('should call signup after OTP verification', () => {
    localStorage.setItem('otp', 'token-abc-123');
    const mockAuthResponse: AuthResponse = { token: 'jwt-auth-token', email: 'john@example.com' };

    authServiceSpy.verifyotp.and.returnValue(of({ otp: '123456', message: 'Verified' }));
    authServiceSpy.signup.and.returnValue(of(mockAuthResponse));

    component.custName = 'John Doe';
    component.mobileNumber = '9876543210';
    component.email = 'john@example.com';
    component.pwd = 'password123';
    component.otp = '123456';

    component.signup();

    expect(authServiceSpy.verifyotp).toHaveBeenCalledWith({
      email: 'john@example.com',
      otpToken: 'token-abc-123',
      otp: '123456'
    });
    expect(authServiceSpy.signup).toHaveBeenCalledWith({
      custName: 'John Doe',
      mobileNumber: '9876543210',
      email: 'john@example.com',
      pwd: 'password123'
    });
  });

  it('should navigate to /dashboard on success', () => {
    localStorage.setItem('otp', 'token-abc-123');
    const mockAuthResponse: AuthResponse = { token: 'jwt-auth-token', email: 'john@example.com' };

    authServiceSpy.verifyotp.and.returnValue(of({ otp: '123456', message: 'Verified' }));
    authServiceSpy.signup.and.returnValue(of(mockAuthResponse));

    component.custName = 'John Doe';
    component.mobileNumber = '9876543210';
    component.email = 'john@example.com';
    component.pwd = 'password123';
    component.otp = '123456';

    component.signup();

    expect(authServiceSpy.saveToken).toHaveBeenCalledWith('jwt-auth-token');
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should show error when otp is invalid length during signup', () => {
    component.otp = '123';
    component.signup();

    expect(component.error).toBe('Please enter valid otp.');
    expect(authServiceSpy.verifyotp).not.toHaveBeenCalled();
  });

  it('should handle verifyotp failure during signup', () => {
    localStorage.setItem('otp', 'token-abc-123');
    authServiceSpy.verifyotp.and.returnValue(throwError(() => new Error('Invalid OTP')));

    component.email = 'john@example.com';
    component.otp = '123456';

    component.signup();

    expect(component.error).toBe('Invalid OTP');
    expect(component.loading).toBeFalse();
    expect(authServiceSpy.signup).not.toHaveBeenCalled();
  });

  it('should handle signup failure when email is already in use', () => {
    localStorage.setItem('otp', 'token-abc-123');
    authServiceSpy.verifyotp.and.returnValue(of({ otp: '123456', message: 'Verified' }));
    authServiceSpy.signup.and.returnValue(throwError(() => new Error('Email exists')));

    component.custName = 'John Doe';
    component.mobileNumber = '9876543210';
    component.email = 'john@example.com';
    component.pwd = 'password123';
    component.otp = '123456';

    component.signup();

    expect(component.error).toBe('Signup failed. Email may already be in use.');
    expect(component.loading).toBeFalse();
    expect(router.navigate).not.toHaveBeenCalled();
  });
});
