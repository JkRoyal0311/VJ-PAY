package com.coforge.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coforge.entities.BankAccount;
import com.coforge.repositories.BankAccountRepository;

@Repository
public class BankAccountDao implements BankAccountDaoInterface {
	@Autowired
	BankAccountRepository bankAccountRepository;
	@Override
	public List<BankAccount> getAllBankAccounts() {
		return bankAccountRepository.findAll();
	}

	@Override
	public List<BankAccount> getAllBankAccountsByQuery(String query) {
		return bankAccountRepository.findBankAccountByQuery(query);
	}

	@Override
	public Optional<BankAccount> getBankAccountByAccountId(long bankAccountId) {
		return bankAccountRepository.findById(bankAccountId);
	}

	@Override
	public BankAccount saveBankAccount(BankAccount bankAccount) {
		return bankAccountRepository.save(bankAccount);
	}

	@Override
	public BankAccount updateBankAccount(BankAccount bankAccount) {
		return bankAccountRepository.save(bankAccount);
	}

	@Override
	public void deleteBankAccount(long bankAccountId) {
		bankAccountRepository.deleteById(bankAccountId);
	}

}
