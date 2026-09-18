package com.coforge.daos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coforge.entities.BillPayment;
import com.coforge.entities.BillType;

public interface BillPaymentDaoInterface {
	public List<BillPayment> getAllBillPayments();
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDate(LocalDateTime start,LocalDateTime end);
	public Optional<BillPayment> getBillPaymentByBillId(long billId);
	public BillPayment saveBillPayment(BillPayment billPayment);
	public BillPayment updateBillPayment(BillPayment billPayment);
	public void deleteBillPayment(long billId);
	public List<BillPayment> getAllBillPaymentsByType(BillType billType);
	public List<BillPayment> getAllBillPaymentsByWalletId(long walletId);
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndWalletId(LocalDateTime start,LocalDateTime end,long walletId);
	public Optional<BillPayment> getBillPaymentByBillIdAndWalletId(long billId, long walletId);
	public List<BillPayment> getAllBillPaymentsByBillTypeAndWalletId(BillType billType, long walletId);
	public List<BillPayment> searchBillPayments(String query);
	List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillType(LocalDateTime start, LocalDateTime end,BillType billtype);
	List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillTypeAndWalletId(LocalDateTime start, LocalDateTime end,BillType billtype,long walletId);
}
