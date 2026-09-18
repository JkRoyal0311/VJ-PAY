package com.coforge.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.coforge.daos.BankAccountDao;
import com.coforge.dtos.BankAccountAdminRequestDto;
import com.coforge.dtos.CustomerJWTTokenDto;
import com.coforge.entities.BankAccount;
import com.coforge.entities.Customer;
import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
import com.coforge.entities.Wallet;
import com.coforge.exception.BankAccountInsufficientBalanceException;
import com.coforge.exception.BankAccountNotFoundException;

@Service
public class BankAccountService implements BankAccountServiceInterface {
	@Autowired
	BankAccountDao bankAccountDao;

	@Autowired
	CustomerService customerService;

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	WalletServiceImpl walletService;
	
	@Override
	public List<BankAccount> getAllBankAccounts() {
		
		return bankAccountDao.getAllBankAccounts();
	}
	
	@Override
	public List<BankAccount> getAllBankAccountsByQuery(String query) {
		return bankAccountDao.getAllBankAccountsByQuery(query);
	}

	@Override
	public BankAccount getBankAccountByAccountId(long bankAccountId) {
		return bankAccountDao.getBankAccountByAccountId(bankAccountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not found"));
	}

	@Override
	public BankAccount saveBankAccountCustomer(BankAccount bankAccount) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());		

		bankAccount.setBalance(100000);
		bankAccount.setIfscCode(bankAccount.getIfscCode().toUpperCase());
		bankAccount.setCustomer(customer);
//		customerService.addBankAccount(customer.getCustId(), bankAccount);
//		System.out.println("added");
		return bankAccountDao.saveBankAccount(bankAccount);
	}

	@Override
	public BankAccount updateBankAccount(long bankAccountId,BankAccount bankAccount) {
		BankAccount bank = getBankAccountByAccountId(bankAccountId);
		bank.setAccountNo(bankAccount.getAccountNo());
		bank.setIfscCode(bankAccount.getIfscCode());
		bank.setBankname(bankAccount.getBankname());
		bank.setBalance(bankAccount.getBalance()==0?bank.getBalance():bankAccount.getBalance());
		return bankAccountDao.saveBankAccount(bank);
	}
	
	public boolean transferToWallet(double amount,long bankAccountId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		
		List<BankAccount> banks = customer.getBankAccounts();
		boolean bankFound = false;
		for(BankAccount bank : banks) {
			if(bank.getBankAccountId() == bankAccountId) {
				bankFound = true;
			}
		}
		
		if(bankFound==false) {
			throw new BankAccountNotFoundException("Bank Account not found");
		}
		
		String description = "Topup of "+amount+" Rs. to wallet";
		Transaction trans = new Transaction(
			    "CREDIT",
			    "PENDING",
			    amount,
			    customer,
			    description,
			    TransactionCategory.WALLET_TOP_UP,
			    TransactionSubCategory.NONE
			);

		Transaction transaction = transactionService.addTransaction(trans);
		try {
			debitFromBankAccount(bankAccountId,amount);
			
			walletService.credit(wallet.getWalletId(), BigDecimal.valueOf(amount));
			
			
			transaction.setTransactionStatus("SUCCESS");
            transactionService.updateTransaction(transaction);
		
			return true;
		}
		catch(Exception e) {
			transaction.setTransactionStatus("FAILED");
            transactionService.updateTransaction(transaction);
            System.out.println(e);
//			return false;
            throw e;
		}
	}

//	@Override
//	public BankAccount debitMoneyFromBankAccount(BankAccount bankAccount) {
//		getBankAccountByAccountId(bankAccount.getBankAccountId());
//		return bankAccountDao.saveBankAccount(bankAccount);
//	}
	
	@Override
	public BankAccount debitFromBankAccount(long bankAccountId,double amount) {
		BankAccount bankAccount = getBankAccountByAccountId(bankAccountId);
		if(bankAccount.getBalance()<amount) {
			throw new BankAccountInsufficientBalanceException("Insuficient Balance");
		}
		bankAccount.setBalance(bankAccount.getBalance()-amount);
		return bankAccountDao.saveBankAccount(bankAccount);
	}

	@Override
	public void deleteBankAccount(long bankAccountId) {
		getBankAccountByAccountId(bankAccountId);
		bankAccountDao.deleteBankAccount(bankAccountId);
	}

	@Override
	public List<BankAccount> getAllBankAccountsOfCustomer() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());
		
		List<BankAccount> banks = customer.getBankAccounts();
		return banks;
	}

	@Override
	public BankAccount updateBankAccountCustomer(long bankAccountId,BankAccount bankAccount) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());
		
		List<BankAccount> banks = customer.getBankAccounts();
		
		for(BankAccount bank : banks) {
			if(bank.getBankAccountId() == bankAccountId) {
				bank.setAccountNo(bankAccount.getAccountNo());
				bank.setIfscCode(bankAccount.getIfscCode());
				bank.setBankname(bankAccount.getBankname());
				bank.setBalance(bankAccount.getBalance()==0?bank.getBalance():bankAccount.getBalance());
				return bankAccountDao.saveBankAccount(bank);
			}
		}
		
		throw new BankAccountNotFoundException("Bank Account not found");
	}

	@Override
	public void deleteBankAccountCustomer(long bankAccountId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());
		
		List<BankAccount> banks = customer.getBankAccounts();
		
		for(BankAccount bank : banks) {
			if(bank.getBankAccountId() == bankAccountId) {
				customer.removeBankAccount(bank);
				customerService.updateCustomer(customer,customer.getCustId());
//				bankAccountDao.deleteBankAccount(bankAccountId);
				return;
			}
		}
		
		throw new BankAccountNotFoundException("Bank Account not found");
	}

	@Override
	public BankAccount getBankAccountByAccountIdCustomer(long bankAccountId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());
		
		List<BankAccount> banks = customer.getBankAccounts();
		
		for(BankAccount bank : banks) {
			if(bank.getBankAccountId() == bankAccountId) {
				return bank;
			}
		}
		
		throw new BankAccountNotFoundException("Bank Account not found");
	}

	@Override
	public BankAccount saveBankAccount(BankAccountAdminRequestDto bankAccountRequestDto) {		
		Customer customer = customerService.getById(bankAccountRequestDto.getCustId());
		BankAccount bankAccount = new BankAccount(bankAccountRequestDto.getAccountNo(),bankAccountRequestDto.getIfscCode(),bankAccountRequestDto.getBankname());
		bankAccount.setBalance(10000);
		bankAccount.setCustomer(customer);
		customerService.addBankAccount(customer.getCustId(), bankAccount);
		
		return bankAccountDao.saveBankAccount(bankAccount);
	}

}
