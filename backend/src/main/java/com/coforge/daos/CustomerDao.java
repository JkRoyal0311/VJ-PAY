package com.coforge.daos;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.coforge.entities.Customer;
import com.coforge.repositories.CustomerRepository;
@Repository
public class CustomerDao implements CustomerDaoInterface
{
	@Autowired
	private CustomerRepository customerRepository;
	@Override
	public List<Customer> getAllCustomer()
	{
		return customerRepository.findByRole("USER");
	}
	@Override
	public Customer saveCustomer(Customer customer)
	{
		return customerRepository.save(customer);
	}
	@Override
	public Optional<Customer> getById(long customerId)
	{
		return customerRepository.findById(customerId);
	}
	@Override
	public Customer updateCustomer(Customer customer)
	{
		return customerRepository.save(customer);
	}
	@Override
	public void deleteCustomer(long customerId)
	{
		customerRepository.deleteById(customerId);
	}
	@Override
	public Optional<Customer> findByEmailAndPwd(String email, String pwd)
	{
		return customerRepository.findByEmailAndPwd(email, pwd);
	}
	@Override
	public List<Customer> findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	@Override
	public List<Customer> findByMobileNumber(String mobileNumber) {
		return customerRepository.findByMobileNumber(mobileNumber);
	}
	@Override
	public List<Customer> findByEmailOrMobileNumber(String email, String mobileNumber) {
		return customerRepository.findByEmailOrMobileNumber(email,mobileNumber);
	}
	@Override
	public List<Customer> findCustomerByQuery(String query) {
		return customerRepository.findCustomerByQuery(query);
	}
}