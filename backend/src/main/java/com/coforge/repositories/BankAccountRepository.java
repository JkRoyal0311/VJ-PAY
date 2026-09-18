package com.coforge.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coforge.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
	
	@Query("SELECT b FROM Customer c JOIN c.bankAccounts b WHERE c.custName LIKE %:query% OR c.mobileNumber LIKE %:query% OR c.email LIKE %:query% OR b.ifscCode LIKE %:query% OR b.bankname LIKE %:query% OR b.ifscCode LIKE %:query% OR b.accountNo LIKE %:query%")
	List<BankAccount> findBankAccountByQuery(@Param("query")String query);
}
