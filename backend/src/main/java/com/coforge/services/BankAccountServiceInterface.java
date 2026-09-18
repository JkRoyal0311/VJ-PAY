package com.coforge.services;

import java.util.List;

import com.coforge.dtos.BankAccountAdminRequestDto;
import com.coforge.entities.BankAccount;

public interface BankAccountServiceInterface {
	public List<BankAccount> getAllBankAccountsOfCustomer();
	public List<BankAccount> getAllBankAccounts();
	public List<BankAccount> getAllBankAccountsByQuery(String query);
	public BankAccount getBankAccountByAccountId(long bankAccountId);
	public BankAccount getBankAccountByAccountIdCustomer(long bankAccountId);
	public BankAccount saveBankAccount(BankAccountAdminRequestDto bankAccount);
	public BankAccount saveBankAccountCustomer(BankAccount bankAccount);
	public BankAccount updateBankAccount(long bankAccountId,BankAccount bankAccount);
	public BankAccount updateBankAccountCustomer(long bankAccountId,BankAccount bankAccount);
	public BankAccount debitFromBankAccount(long bankAccountId,double amount);
	public boolean transferToWallet(double amount,long bankAccountId);
	public void deleteBankAccount(long bankAccountId);
	public void deleteBankAccountCustomer(long bankAccountId);
}
