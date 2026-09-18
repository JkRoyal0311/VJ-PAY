package com.coforge.daos;

import java.time.LocalDate;
import java.util.List;

import com.coforge.entities.Customer;
import com.coforge.entities.Transaction;

public interface TransactionDaoInterface {
	public Customer findCustomerById(long id);
	public Transaction saveTransaction(Transaction tx);
	 public List<Transaction> viewAllTransaction();
	  public Transaction findTransactionById(long id);
    public List<Transaction> viewTransactionByDate(LocalDate from, LocalDate to);
	List<Transaction> viewTransactionByMonth(int month, int year);

}
