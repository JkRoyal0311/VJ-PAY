import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { WalletService } from './wallet.service';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import { Wallet, WalletBalance } from '../models/wallet.model';

describe('WalletService', () => {
  let service: WalletService;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getAuthHeaders']);
    authServiceSpy.getAuthHeaders.and.returnValue({ Authorization: 'Bearer test-token' });

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        WalletService,
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });

    service = TestBed.inject(WalletService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call GET for getWallet()', () => {
    const mockWallet: Wallet = {
      walletId: 10,
      balance: 1500,
      beneficiary: []
    };

    service.getWallet().subscribe((wallet) => {
      expect(wallet).toEqual(mockWallet);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/auth/wallets`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token');
    req.flush(mockWallet);
  });

  it('should call GET for getBalance()', () => {
    const mockBalance: WalletBalance = { balance: 3500 };
    const walletId = 10;

    service.getBalance(walletId).subscribe((res) => {
      expect(res).toEqual(mockBalance);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/auth/wallets/${walletId}/balance`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token');
    req.flush(mockBalance);
  });
});
