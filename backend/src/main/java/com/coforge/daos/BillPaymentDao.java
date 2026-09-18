package com.coforge.daos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coforge.entities.BillPayment;
import com.coforge.entities.BillType;
import com.coforge.repositories.BillPaymentRepository;

@Repository
public class BillPaymentDao implements BillPaymentDaoInterface {
	@Autowired
	BillPaymentRepository billPaymentRepository;

	@Override
	public List<BillPayment> getAllBillPayments() {
		return billPaymentRepository.findAll();
	}

	@Override
	public Optional<BillPayment> getBillPaymentByBillId(long billId) {
		return billPaymentRepository.findById(billId);
	}

	@Override
	public BillPayment saveBillPayment(BillPayment billPayment) {
		return billPaymentRepository.save(billPayment);
	}

	@Override
	public BillPayment updateBillPayment(BillPayment billPayment) {
		return billPaymentRepository.save(billPayment);
	}

	@Override
	public void deleteBillPayment(long billId) {
		billPaymentRepository.deleteById(billId);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsByType(BillType billType) {
		return billPaymentRepository.findByBillType(billType);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsByWalletId(long walletId) {
		return billPaymentRepository.findAllByWalletWalletId(walletId);
	}

	@Override
	public Optional<BillPayment> getBillPaymentByBillIdAndWalletId(long billId, long walletId) {
		return billPaymentRepository.findByBillIdAndWalletWalletId(billId,walletId);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsByBillTypeAndWalletId(BillType billType, long walletId) {
		return billPaymentRepository.findAllByBillTypeAndWalletWalletId(billType, walletId);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDate(LocalDateTime start, LocalDateTime end) {
		return billPaymentRepository.findAllByPaymentDateBetween(start, end);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillType(LocalDateTime start, LocalDateTime end,BillType billtype) {
		return billPaymentRepository.findAllByPaymentDateBetweenAndBillType(start, end,billtype);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndWalletId(LocalDateTime start, LocalDateTime end,
			long walletId) {
		return billPaymentRepository.findAllByPaymentDateBetweenAndWalletWalletId(start, end, walletId);
	}

	@Override
	public List<BillPayment> searchBillPayments(String query) {
		return billPaymentRepository.searchBillPayments(query);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillTypeAndWalletId(LocalDateTime start,
			LocalDateTime end, BillType billtype, long walletId) {
		
		return billPaymentRepository.findAllByPaymentDateBetweenAndBillTypeAndWalletWalletId(start, end, billtype, walletId);
	}

}
