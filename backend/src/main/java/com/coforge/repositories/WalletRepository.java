package com.coforge.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coforge.entities.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	@Query("SELECT w FROM Wallet w JOIN w.customer c WHERE c.custId = :custId")
	Optional<Wallet> findWalletByCustomerId(@Param("custId") Long childId);
	
	@Query("SELECT w FROM Wallet w JOIN w.customer c WHERE c.custName LIKE %:query% OR c.mobileNumber LIKE %:query% OR c.email LIKE %:query%")
	List<Wallet> searchWallet(@Param("query")String query);
}
