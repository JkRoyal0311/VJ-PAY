package com.coforge.daos;
import java.util.List;
import java.util.Optional;
import com.coforge.entities.Customer;
public interface CustomerDaoInterface
{
	public List<Customer> getAllCustomer();
	public Customer saveCustomer(Customer customer);
	public Optional<Customer> getById(long customerId);
	public void deleteCustomer(long itemId);
	public Optional<Customer> findByEmailAndPwd(String email, String pwd);
	public List<Customer> findByEmail(String email);
	public List<Customer> findByMobileNumber(String mobileNumber);
	public List<Customer> findByEmailOrMobileNumber(String email,String mobileNumber);
	public List<Customer> findCustomerByQuery(String query);
	Customer updateCustomer(Customer customer);
}