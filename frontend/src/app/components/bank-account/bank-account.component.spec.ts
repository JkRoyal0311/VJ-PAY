import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { BankAccountComponent } from './bank-account.component';
import { BankAccountService } from '../../services/bank-account.service';
import { PaymentService } from '../../services/payment.service';
import { BankAccount } from '../../models/bank-account.model';

describe('BankAccountComponent', () => {
  let component: BankAccountComponent;
  let fixture: ComponentFixture<BankAccountComponent>;
  let bankServiceSpy: jasmine.SpyObj<BankAccountService>;
  let paymentServiceSpy: jasmine.SpyObj<PaymentService>;

  const mockAccounts: BankAccount[] = [
    {
      bankAccountId: 1,
      accountNo: '123456789012',
      ifscCode: 'HDFC0001234',
      bankname: 'HDFC Bank',
      balance: 25000
    },
    {
      bankAccountId: 2,
      accountNo: '987654321098',
      ifscCode: 'ICIC0005678',
      bankname: 'ICICI Bank',
      balance: 40000
    }
  ];

  beforeEach(async () => {
    bankServiceSpy = jasmine.createSpyObj('BankAccountService', [
      'getAll',
      'add',
      'delete',
      'transferToWallet'
    ]);
    paymentServiceSpy = jasmine.createSpyObj('PaymentService', [
      'createOrder',
      'verifyPayment'
    ]);

    bankServiceSpy.getAll.and.returnValue(of(mockAccounts));

    await TestBed.configureTestingModule({
      imports: [BankAccountComponent],
      providers: [
        { provide: BankAccountService, useValue: bankServiceSpy },
        { provide: PaymentService, useValue: paymentServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BankAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load accounts on init', () => {
    expect(bankServiceSpy.getAll).toHaveBeenCalled();
    expect(component.accounts).toEqual(mockAccounts);
    expect(component.loading).toBeFalse();
  });

  it('should toggle add form', () => {
    expect(component.showForm).toBeFalse();
    component.showForm = !component.showForm;
    expect(component.showForm).toBeTrue();
    component.showForm = !component.showForm;
    expect(component.showForm).toBeFalse();
  });

  it('should call addAccount and refresh', () => {
    const newAccount: BankAccount = {
      bankAccountId: 3,
      accountNo: '112233445566',
      ifscCode: 'SBIN0009999',
      bankname: 'SBI Bank',
      balance: 10000
    };
    bankServiceSpy.add.and.returnValue(of(newAccount));
    bankServiceSpy.getAll.calls.reset();
    bankServiceSpy.getAll.and.returnValue(of([...mockAccounts, newAccount]));

    component.form = {
      accountNo: '112233445566',
      ifscCode: 'SBIN0009999',
      bankname: 'SBI Bank'
    };
    component.showForm = true;

    component.addAccount();

    expect(bankServiceSpy.add).toHaveBeenCalledWith({
      accountNo: '112233445566',
      ifscCode: 'SBIN0009999',
      bankname: 'SBI Bank'
    });
    expect(component.successMsg).toBe('Account added successfully.');
    expect(component.showForm).toBeFalse();
    expect(component.form).toEqual({ accountNo: '', ifscCode: '', bankname: '' });
    expect(bankServiceSpy.getAll).toHaveBeenCalled();
  });

  it('should call deleteAccount', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    bankServiceSpy.delete.and.returnValue(of('Account deleted'));
    bankServiceSpy.getAll.calls.reset();
    bankServiceSpy.getAll.and.returnValue(of([mockAccounts[1]]));

    component.deleteAccount(1);

    expect(window.confirm).toHaveBeenCalledWith('Delete this bank account?');
    expect(bankServiceSpy.delete).toHaveBeenCalledWith(1);
    expect(component.successMsg).toBe('Account deleted.');
    expect(bankServiceSpy.getAll).toHaveBeenCalled();
  });

  it('should not call deleteAccount if user cancels confirm', () => {
    spyOn(window, 'confirm').and.returnValue(false);

    component.deleteAccount(1);

    expect(window.confirm).toHaveBeenCalled();
    expect(bankServiceSpy.delete).not.toHaveBeenCalled();
  });

  it('should open transfer modal', () => {
    const account = mockAccounts[0];
    component.openTransfer(account);

    expect(component.transferModal).toEqual(account);
    expect(component.transferAmount).toBe(0);
  });

  it('should handle addAccount error', () => {
    bankServiceSpy.add.and.returnValue(throwError(() => new Error('Error')));

    component.form = {
      accountNo: '112233445566',
      ifscCode: 'SBIN0009999',
      bankname: 'SBI Bank'
    };
    component.addAccount();

    expect(component.error).toBe('Failed to add account. Please Enter Valid Information');
  });

  it('should handle load accounts error', () => {
    bankServiceSpy.getAll.and.returnValue(throwError(() => new Error('Load failed')));
    component.load();

    expect(component.error).toBe('Failed to load accounts.');
    expect(component.loading).toBeFalse();
  });
});
