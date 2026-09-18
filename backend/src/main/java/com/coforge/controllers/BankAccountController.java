package com.coforge.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.dtos.BankAccountDto;
import com.coforge.entities.BankAccount;
import com.coforge.services.BankAccountService;

@RestController
@RequestMapping("/auth/bankAccount")
@CrossOrigin
public class BankAccountController {
	@Autowired
	BankAccountService bankAccountService;
	
	@GetMapping("")
	public ResponseEntity<List<BankAccountDto>> getBankAccounts(){
		List<BankAccountDto> banks = bankAccountService.getAllBankAccountsOfCustomer().stream().map((b)->new BankAccountDto(b.getBankAccountId(),b.getAccountNo(),b.getIfscCode(),b.getBankname(),b.getBalance())).collect(Collectors.toList());
		return new ResponseEntity<>(banks,HttpStatus.OK);
	}

	@GetMapping("/{bankAccountId}")
	public ResponseEntity<BankAccountDto> getBankAccountById(@PathVariable("bankAccountId") long bankAccountId){
		BankAccount bank = bankAccountService.getBankAccountByAccountIdCustomer(bankAccountId);
		return new ResponseEntity<>(new BankAccountDto(bank.getBankAccountId(),bank.getAccountNo(),bank.getIfscCode(),bank.getBankname(),bank.getBalance()),HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<BankAccountDto> addBankAccounts(@RequestBody BankAccount bankAccount){
		BankAccount bank = bankAccountService.saveBankAccountCustomer(bankAccount);
		return new ResponseEntity<>(new BankAccountDto(bank.getBankAccountId(),bank.getAccountNo(),bank.getIfscCode(),bank.getBankname(),bank.getBalance()),HttpStatus.OK);
	}

	@PutMapping("/{bankAccountId}")
	public ResponseEntity<BankAccountDto> updateBankAccount(@PathVariable("bankAccountId") long bankAccountId,@RequestBody BankAccount bankAccount){
		BankAccount bank = bankAccountService.updateBankAccountCustomer(bankAccountId,bankAccount);
		return new ResponseEntity<>(new BankAccountDto(bank.getBankAccountId(),bank.getAccountNo(),bank.getIfscCode(),bank.getBankname(),bank.getBalance()),HttpStatus.OK);
	}

	@DeleteMapping("/{bankAccountId}")
	public ResponseEntity<Map<String,String>> deleteBankAccounts(@PathVariable("bankAccountId") long bankAccountId){
		bankAccountService.deleteBankAccountCustomer(bankAccountId);
		return new ResponseEntity<>(Map.of("message","Bank Account Deleted"),HttpStatus.OK);
	}
	
	@PostMapping("/transferToWallet/{bankAccountId}")
    public ResponseEntity<Map<String,String>> topUpWallet(@Param("amount") double amount,@PathVariable("bankAccountId") long bankAccountId) {
        boolean success = bankAccountService.transferToWallet(amount,bankAccountId);
        String response="";
        if(success) {
        	response = amount + " Rs. Transferred to wallet";
        }
        else {
        	response = "Failed to transfer "+amount + " Rs. to wallet";
        }
        return new ResponseEntity<>(Map.of("message",response),HttpStatus.CREATED);
    }
}
