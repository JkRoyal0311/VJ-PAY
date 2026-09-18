package com.coforge.daos;

import java.util.List;
import java.util.Optional;

import com.coforge.entities.BankAccount;

public interface BankAccountDaoInterface {
	public List<BankAccount> getAllBankAccounts();
	public List<BankAccount> getAllBankAccountsByQuery(String query);
	public Optional<BankAccount> getBankAccountByAccountId(long bankAccountId);
	public BankAccount saveBankAccount(BankAccount bankAccount);
	public BankAccount updateBankAccount(BankAccount bankAccount);
	public void deleteBankAccount(long bankAccountId);
}
