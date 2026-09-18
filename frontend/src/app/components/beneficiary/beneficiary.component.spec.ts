import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { BeneficiaryComponent } from './beneficiary.component';
import { BeneficiaryService } from '../../services/beneficiary.service';
import { PaymentService, PaymentOrderResponse } from '../../services/payment.service';
import { Beneficiary } from '../../models/beneficiary.model';

describe('BeneficiaryComponent', () => {
  let component: BeneficiaryComponent;
  let fixture: ComponentFixture<BeneficiaryComponent>;
  let beneficiaryServiceSpy: jasmine.SpyObj<BeneficiaryService>;
  let paymentServiceSpy: jasmine.SpyObj<PaymentService>;

  const mockBeneficiaries: Beneficiary[] = [
    {
      beneficiaryId: 1,
      beneficiaryName: 'Alice Smith',
      mobileNumber: '9876543210'
    },
    {
      beneficiaryId: 2,
      beneficiaryName: 'Bob Jones',
      mobileNumber: '9123456789'
    }
  ];

  beforeEach(async () => {
    beneficiaryServiceSpy = jasmine.createSpyObj('BeneficiaryService', [
      'getAll',
      'add',
      'delete',
      'sendMoney'
    ]);
    paymentServiceSpy = jasmine.createSpyObj('PaymentService', [
      'createOrder',
      'verifyPayment'
    ]);

    beneficiaryServiceSpy.getAll.and.returnValue(of(mockBeneficiaries));

    await TestBed.configureTestingModule({
      imports: [BeneficiaryComponent],
      providers: [
        { provide: BeneficiaryService, useValue: beneficiaryServiceSpy },
        { provide: PaymentService, useValue: paymentServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BeneficiaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load beneficiaries on init', () => {
    expect(beneficiaryServiceSpy.getAll).toHaveBeenCalled();
    expect(component.beneficiaries).toEqual(mockBeneficiaries);
    expect(component.loading).toBeFalse();
  });

  it('should add beneficiary and refresh', () => {
    beneficiaryServiceSpy.add.and.returnValue(of('Beneficiary Added'));
    beneficiaryServiceSpy.getAll.calls.reset();
    beneficiaryServiceSpy.getAll.and.returnValue(of(mockBeneficiaries));

    component.form = {
      beneficiaryName: 'Charlie Brown',
      mobileNumber: '9988776655'
    };
    component.showForm = true;

    component.addBeneficiary();

    expect(beneficiaryServiceSpy.add).toHaveBeenCalledWith({
      beneficiaryName: 'Charlie Brown',
      mobileNumber: '9988776655'
    });
    expect(component.successMsg).toBe('Beneficiary Added');
    expect(component.showForm).toBeFalse();
    expect(component.form).toEqual({ beneficiaryName: '', mobileNumber: '' });
    expect(beneficiaryServiceSpy.getAll).toHaveBeenCalled();
  });

  it('should validate mobile number before adding beneficiary', () => {
    component.form = {
      beneficiaryName: 'Invalid Phone',
      mobileNumber: '123'
    };

    component.addBeneficiary();

    expect(component.error).toBe('Mobile number must be exactly 10 digits.');
    expect(beneficiaryServiceSpy.add).not.toHaveBeenCalled();
  });

  it('should delete beneficiary', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    beneficiaryServiceSpy.delete.and.returnValue(of('Deleted'));
    beneficiaryServiceSpy.getAll.calls.reset();
    beneficiaryServiceSpy.getAll.and.returnValue(of([mockBeneficiaries[1]]));

    component.delete(1);

    expect(window.confirm).toHaveBeenCalledWith('Delete this beneficiary?');
    expect(beneficiaryServiceSpy.delete).toHaveBeenCalledWith(1);
    expect(component.successMsg).toBe('Beneficiary Deleted');
    expect(beneficiaryServiceSpy.getAll).toHaveBeenCalled();
  });

  it('should not delete beneficiary if user cancels confirmation', () => {
    spyOn(window, 'confirm').and.returnValue(false);

    component.delete(1);

    expect(window.confirm).toHaveBeenCalled();
    expect(beneficiaryServiceSpy.delete).not.toHaveBeenCalled();
  });

  it('should open send modal', () => {
    const beneficiary = mockBeneficiaries[0];
    component.openSend(beneficiary);

    expect(component.sendModal).toEqual(beneficiary);
    expect(component.sendAmount).toBe(0);
  });

  it('should call doSend', () => {
    const mockOrder: PaymentOrderResponse = {
      orderId: 'order_123',
      amount: 500,
      currency: 'INR',
      keyId: 'rzp_test_123'
    };
    paymentServiceSpy.createOrder.and.returnValue(of(mockOrder));

    const mockRzpInstance = { open: jasmine.createSpy('open') };
    (window as any).Razorpay = jasmine.createSpy('Razorpay').and.returnValue(mockRzpInstance);

    component.sendModal = mockBeneficiaries[0];
    component.sendAmount = 500;

    component.doSend();

    expect(paymentServiceSpy.createOrder).toHaveBeenCalledWith({
      amount: 500,
      currency: 'INR'
    });
    expect((window as any).Razorpay).toHaveBeenCalled();
    expect(mockRzpInstance.open).toHaveBeenCalled();
  });

  it('should not call doSend if sendModal is null or sendAmount <= 0', () => {
    component.sendModal = null;
    component.sendAmount = 0;
    component.doSend();

    expect(paymentServiceSpy.createOrder).not.toHaveBeenCalled();

    component.sendModal = mockBeneficiaries[0];
    component.sendAmount = 0;
    component.doSend();

    expect(paymentServiceSpy.createOrder).not.toHaveBeenCalled();
  });
});
