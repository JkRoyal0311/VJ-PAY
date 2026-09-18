package com.coforge.repositories;
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coforge.entities.BillPayment;
import com.coforge.entities.Customer;
//import com.coforge.entities.Beneficiary;
import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
		
	    List<Transaction> findByCategory(TransactionCategory category);
	    List<Transaction> findByCategoryAndCustomerCustId(TransactionCategory category,long customer);
	    List<Transaction> findByCategoryAndTransactionDateBetweenAndCustomerCustId(TransactionCategory category,LocalDate from, LocalDate to,long customer);
	    List<Transaction> findBySubCategoryAndTransactionDateBetweenAndCustomerCustId(TransactionSubCategory subCategory,LocalDate from, LocalDate to,long customer);

	    List<Transaction> findByCategoryAndTransactionDateBetween(TransactionCategory category,LocalDate from, LocalDate to);
	    List<Transaction> findBySubCategoryAndTransactionDateBetween(TransactionSubCategory subCategory,LocalDate from, LocalDate to);

	    List<Transaction> findBySubCategory(TransactionSubCategory subCategory);
	    List<Transaction> findBySubCategoryAndCustomerCustId(TransactionSubCategory subCategory,long customer);

	    List<Transaction> findByCustomer_CustId(Long custId);
 
	    List<Transaction> findByCustomer_CustIdAndCategory(Long custId, TransactionCategory category);
 
	    List<Transaction> findByCustomer_CustIdAndSubCategory(Long custId, TransactionSubCategory subCategory);
	    List<Transaction> findByTransactionDateBetween(LocalDate from, LocalDate to);
	    List<Transaction> findByCustomerCustIdAndTransactionDateBetween(long custId,LocalDate from, LocalDate to);
 
@Query("SELECT t FROM Transaction t WHERE MONTH(t.transactionDate) = :month AND YEAR(t.transactionDate) = :year")
List<Transaction> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
List<Transaction> findAllByCustomerCustId(long customer);
		
		
		void deleteByCustomerCustId(long custId);
//		@Query("SELECT t FROM Trasnaction t JOIN t.customer c WHERE c.custName LIKE %:query% OR c.mobileNumber LIKE %:query% OR c.email LIKE %:query%")
//		List<Transaction> searchTransactions(@Param("query") String query);
 
		@Query("SELECT t FROM Transaction t JOIN t.customer c WHERE c.custName LIKE %:query% OR c.mobileNumber LIKE %:query% OR c.email LIKE %:query%")
		public List<Transaction> searchTransactions(@Param("query") String  query);

}
