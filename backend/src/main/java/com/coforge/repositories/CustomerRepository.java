package com.coforge.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coforge.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{
	Optional<Customer> findByEmailAndPwd(String email, String pwd);
	
	List<Customer> findByRole(String role);

	List<Customer> findByEmail(String email);
	
	List<Customer> findByMobileNumber(String mobileNumber);

	List<Customer> findByEmailOrMobileNumber(String email,String mobileNumber);

	@Query("SELECT c FROM Customer c WHERE c.custName LIKE %:query% OR c.mobileNumber LIKE %:query% OR c.email LIKE %:query%")
	List<Customer> findCustomerByQuery(@Param("query") String query);

}

