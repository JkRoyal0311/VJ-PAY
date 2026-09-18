package com.coforge.services;
import java.util.List;

import com.coforge.dtos.CustomerDto;
import com.coforge.dtos.CustomerResponseDto;
import com.coforge.entities.BankAccount;
import com.coforge.entities.Customer;

public interface CustomerServiceInterface
{
	public List<CustomerResponseDto> getAllCustomer();
	public CustomerDto getDetails();
	public Customer saveCustomer(Customer customer);
	public CustomerResponseDto loginAdmin(String email,String pwd);
	public Customer getById(long customerId);
	public CustomerDto getCustomerDtoById(long customerId);
	public CustomerDto updateCustomer(Customer customer, long id);
	public void deleteCustomer(long itemId);
	public Customer addBankAccount(long customerId,BankAccount bankAccount);
	public Customer login(String email, String pwd);
	public List<Customer> findByEmail(String email);
	public List<Customer> findByMobileNumber(String mobileNumber);
	public List<Customer> findByEmailOrMobileNumber(String email,String mobileNumber);
	public List<CustomerDto> findCustomerByQuery(String query);
	Customer findSingleCustomerByEmail(String email);
	Customer updateCustomerPassword(String pwd, String newpwd);
	Customer updateCustomerPasswordByEmail(String email, String newPwd);
}