package com.coforge.services;

import java.time.LocalDateTime;
import java.util.List;

import com.coforge.dtos.BillPaymentRequestDto;
import com.coforge.entities.BillPayment;
import com.coforge.entities.BillType;

public interface BillPaymentServiceInterface {
	public List<BillPayment> getAllBillPayments();
	public List<BillPayment> getAllBillPaymentsByBillType(BillType billType);
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDate(LocalDateTime start,LocalDateTime end);
	public BillPayment getBillPaymentByBillId(long billId);
	public BillPayment createBillPayment(BillPaymentRequestDto billPaymentRequestDto);
//	public BillPayment updateBillPayment(BillPayment billPayment);
	public void deleteBillPayment(long billId);
	public List<BillPayment> searchBillPayments(String query);

	public List<BillPayment> getAllBillPaymentsCustomer();
	public List<BillPayment> getAllBillPaymentsByBillTypeCustomer(BillType billType);
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateCustomer(LocalDateTime start,LocalDateTime end);
	public BillPayment getBillPaymentByBillIdCustomer(long billId);
//	public BillPayment updateBillPaymentCustomer(BillPayment billPayment);
	public void deleteBillPaymentCustomer(long billId);
	List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillType(LocalDateTime start, LocalDateTime end,BillType billtype);
	List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillTypeCustomer(LocalDateTime start, LocalDateTime end,BillType billtype);

	
}
