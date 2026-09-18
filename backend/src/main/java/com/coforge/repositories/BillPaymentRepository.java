package com.coforge.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coforge.entities.BillPayment;
import com.coforge.entities.BillType;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
	public List<BillPayment> findByBillType(BillType billtype);
	
	public List<BillPayment> findAllByWalletWalletId(long walletId);
	public Optional<BillPayment> findByBillIdAndWalletWalletId(long billId, long walletId);
	public List<BillPayment> findAllByBillTypeAndWalletWalletId(BillType billType, long walletId);
	
	public List<BillPayment> findAllByPaymentDateBetween(LocalDateTime start, LocalDateTime end);
	public List<BillPayment> findAllByPaymentDateBetweenAndBillType(LocalDateTime start, LocalDateTime end,BillType billtype);
	public List<BillPayment> findAllByPaymentDateBetweenAndBillTypeAndWalletWalletId(LocalDateTime start, LocalDateTime end,BillType billtype,long walletId);
	public List<BillPayment> findAllByPaymentDateBetweenAndWalletWalletId(LocalDateTime start, LocalDateTime end, long walletId);
	
	@Query("SELECT b FROM BillPayment b JOIN b.wallet w JOIN w.customer c WHERE c.custName LIKE %:query% OR c.mobileNumber LIKE %:query% OR c.email LIKE %:query%")
	public List<BillPayment> searchBillPayments(@Param("query") String  query);

}
