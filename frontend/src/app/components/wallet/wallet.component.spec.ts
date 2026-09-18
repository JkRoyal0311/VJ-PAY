import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { WalletComponent } from './wallet.component';
import { WalletService } from '../../services/wallet.service';
import { BankAccountService } from '../../services/bank-account.service';
import { PaymentService } from '../../services/payment.service';
import { Wallet } from '../../models/wallet.model';
import { BankAccount } from '../../models/bank-account.model';

describe('WalletComponent', () => {
  let component: WalletComponent;
  let fixture: ComponentFixture<WalletComponent>;
  let walletServiceSpy: jasmine.SpyObj<WalletService>;
  let bankServiceSpy: jasmine.SpyObj<BankAccountService>;
  let paymentServiceSpy: jasmine.SpyObj<PaymentService>;

  const mockWallet: Wallet = {
    walletId: 10,
    balance: 2500,
    beneficiary: []
  };

  const mockAccounts: BankAccount[] = [
    {
      bankAccountId: 101,
      accountNo: '123456789012',
      ifscCode: 'HDFC0001234',
      bankname: 'HDFC Bank',
      balance: 50000
    },
    {
      bankAccountId: 102,
      accountNo: '987654321098',
      ifscCode: 'SBIN0005678',
      bankname: 'State Bank of India',
      balance: 30000
    }
  ];

  beforeEach(async () => {
    walletServiceSpy = jasmine.createSpyObj('WalletService', ['getWallet']);
    bankServiceSpy = jasmine.createSpyObj('BankAccountService', ['getAll', 'transferToWallet']);
    paymentServiceSpy = jasmine.createSpyObj('PaymentService', ['createOrder', 'verifyPayment']);

    walletServiceSpy.getWallet.and.returnValue(of(mockWallet));
    bankServiceSpy.getAll.and.returnValue(of(mockAccounts));

    await TestBed.configureTestingModule({
      imports: [WalletComponent],
      providers: [
        { provide: WalletService, useValue: walletServiceSpy },
        { provide: BankAccountService, useValue: bankServiceSpy },
        { provide: PaymentService, useValue: paymentServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(WalletComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load wallet on init', () => {
    expect(walletServiceSpy.getWallet).toHaveBeenCalled();
    expect(component.wallet).toEqual(mockWallet);
    expect(component.loading).toBeFalse();
  });

  it('should show bank accounts when showBankAccounts is called', () => {
    component.showBankAccounts();

    expect(component.showTopUpAccount).toBeTrue();
    expect(bankServiceSpy.getAll).toHaveBeenCalled();
    expect(component.accounts).toEqual(mockAccounts);
    expect(component.bankloading).toBeFalse();
  });

  it('should open transfer modal', () => {
    const account = mockAccounts[0];
    component.openTransfer(account);

    expect(component.transferModal).toEqual(account);
    expect(component.transferAmount).toBe(0);
  });

  it('should handle wallet load failure', () => {
    walletServiceSpy.getWallet.and.returnValue(throwError(() => new Error('Failed')));
    component.load();

    expect(component.error).toBe('Failed to load wallet.');
    expect(component.loading).toBeFalse();
  });

  it('should handle bank accounts load failure', () => {
    bankServiceSpy.getAll.and.returnValue(throwError(() => new Error('Failed to load banks')));
    component.loadBanks();

    expect(component.bankError).toBe('Failed to load accounts.');
    expect(component.bankloading).toBeFalse();
  });
});
