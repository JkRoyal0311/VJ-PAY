import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { DashboardComponent } from './dashboard.component';
import { CustomerService } from '../../services/customer.service';
import { TransactionService } from '../../services/transaction.service';
import { Customer } from '../../models/customer.model';
import { Transaction } from '../../models/transaction.model';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let customerServiceSpy: jasmine.SpyObj<CustomerService>;
  let txServiceSpy: jasmine.SpyObj<TransactionService>;

  const mockCustomer: Customer = {
    custId: 1,
    custName: 'John Doe',
    mobileNumber: '9876543210',
    email: 'john@example.com',
    wallet: {
      walletId: 10,
      balance: 5000,
      beneficiary: []
    }
  };

  const mockTransactions: Transaction[] = [
    {
      transactionId: 1,
      transactionType: 'DEBIT',
      transactionStatus: 'SUCCESS',
      transactionAmount: 100,
      customerId: 1,
      category: 'BILL_PAYMENT',
      subCategory: 'ELECTRICITY',
      description: 'Electricity Bill',
      transactionDate: '2026-03-01'
    },
    {
      transactionId: 2,
      transactionType: 'CREDIT',
      transactionStatus: 'SUCCESS',
      transactionAmount: 200,
      customerId: 1,
      category: 'WALLET_TOP_UP',
      subCategory: 'MOBILE_RECHARGE',
      description: 'Mobile Recharge',
      transactionDate: '2026-03-02'
    },
    {
      transactionId: 3,
      transactionType: 'DEBIT',
      transactionStatus: 'SUCCESS',
      transactionAmount: 300,
      customerId: 1,
      category: 'BENEFICIARY_TRANSFER',
      subCategory: 'GAS',
      description: 'Gas Bill',
      transactionDate: '2026-03-03'
    },
    {
      transactionId: 4,
      transactionType: 'CREDIT',
      transactionStatus: 'SUCCESS',
      transactionAmount: 400,
      customerId: 1,
      category: 'WALLET_TOP_UP',
      subCategory: 'ELECTRICITY',
      description: 'Top up',
      transactionDate: '2026-03-04'
    },
    {
      transactionId: 5,
      transactionType: 'DEBIT',
      transactionStatus: 'SUCCESS',
      transactionAmount: 500,
      customerId: 1,
      category: 'BILL_PAYMENT',
      subCategory: 'GAS',
      description: 'Gas Bill 2',
      transactionDate: '2026-03-05'
    },
    {
      transactionId: 6,
      transactionType: 'DEBIT',
      transactionStatus: 'SUCCESS',
      transactionAmount: 600,
      customerId: 1,
      category: 'BILL_PAYMENT',
      subCategory: 'ELECTRICITY',
      description: 'Electricity Bill 2',
      transactionDate: '2026-03-06'
    }
  ];

  beforeEach(async () => {
    customerServiceSpy = jasmine.createSpyObj('CustomerService', ['getDetails', 'resetPassword']);
    txServiceSpy = jasmine.createSpyObj('TransactionService', ['getAll']);

    customerServiceSpy.getDetails.and.returnValue(of(mockCustomer));
    txServiceSpy.getAll.and.returnValue(of(mockTransactions));

    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        { provide: CustomerService, useValue: customerServiceSpy },
        { provide: TransactionService, useValue: txServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load customer details on init', () => {
    expect(customerServiceSpy.getDetails).toHaveBeenCalled();
    expect(component.customer).toEqual(mockCustomer);
    expect(component.loading).toBeFalse();
  });

  it('should load recent transactions', () => {
    expect(txServiceSpy.getAll).toHaveBeenCalled();
    expect(component.recentTransactions.length).toBe(5);
    expect(component.recentTransactions).toEqual(mockTransactions.slice(0, 5));
  });

  it('should toggle reset password form', () => {
    expect(component.resetPwdForm).toBeFalse();
    component.showResetPasswordForm();
    expect(component.resetPwdForm).toBeTrue();
    expect(component.pwd).toBe('');
    expect(component.newPwd).toBe('');

    component.showResetPasswordForm();
    expect(component.resetPwdForm).toBeFalse();
  });

  it('should call resetPassword and handle success', () => {
    const updatedCustomer: Customer = { ...mockCustomer, custName: 'Updated Name' };
    customerServiceSpy.resetPassword.and.returnValue(of(updatedCustomer));

    component.pwd = 'oldSecret';
    component.newPwd = 'newSecret';
    component.resetPassword();

    expect(customerServiceSpy.resetPassword).toHaveBeenCalledWith({
      pwd: 'oldSecret',
      newPwd: 'newSecret'
    });
    expect(component.resetPwdSuccessMessage).toBe('Password Updated Successfully');
    expect(component.customer).toEqual(updatedCustomer);
    expect(component.resetPwdLoading).toBeFalse();
    expect(component.resetPwdForm).toBeFalse();
  });

  it('should handle customer details load error on init', () => {
    customerServiceSpy.getDetails.and.returnValue(throwError(() => new Error('Load failed')));
    component.ngOnInit();

    expect(component.error).toBe('Failed to load customer details.');
    expect(component.loading).toBeFalse();
  });

  it('should handle resetPassword failure', () => {
    customerServiceSpy.resetPassword.and.returnValue(throwError(() => new Error('Invalid pwd')));

    component.pwd = 'wrongPass';
    component.newPwd = 'newPass';
    component.resetPassword();

    expect(customerServiceSpy.resetPassword).toHaveBeenCalledWith({
      pwd: 'wrongPass',
      newPwd: 'newPass'
    });
    expect(component.resetPwdError).toBe('Invalid Current Password');
    expect(component.resetPwdLoading).toBeFalse();
  });
});
