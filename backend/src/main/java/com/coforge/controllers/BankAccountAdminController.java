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

import com.coforge.dtos.BankAccountAdminRequestDto;
import com.coforge.dtos.BankAccountDto;
import com.coforge.entities.BankAccount;
import com.coforge.services.BankAccountService;

@RestController
@RequestMapping("/admin/bankAccount")
@CrossOrigin
public class BankAccountAdminController {
	@Autowired
	BankAccountService bankAccountService;
	
	@GetMapping("")
	public ResponseEntity<List<BankAccountDto>> getBankAccounts(){
		List<BankAccountDto> banks = bankAccountService.getAllBankAccounts().stream().map((b)->new BankAccountDto(b.getBankAccountId(),b.getAccountNo(),b.getIfscCode(),b.getBankname(),b.getBalance())).collect(Collectors.toList());
		return new ResponseEntity<>(banks,HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<BankAccountDto> addBankAccounts(@RequestBody BankAccountAdminRequestDto bankAccountRequestDto){
		BankAccount bank = bankAccountService.saveBankAccount(bankAccountRequestDto);
		return new ResponseEntity<>(new BankAccountDto(bank.getBankAccountId(),bank.getAccountNo(),bank.getIfscCode(),bank.getBankname(),bank.getBalance()),HttpStatus.OK);
	}

	@PutMapping("/{bankAccountId}")
	public ResponseEntity<BankAccountDto> updateBankAccount(@RequestBody BankAccount bankAccount,@PathVariable("bankAccountId") long bankAccountId){
		BankAccount bank = bankAccountService.updateBankAccount(bankAccountId,bankAccount);
		return new ResponseEntity<>(new BankAccountDto(bank.getBankAccountId(),bank.getAccountNo(),bank.getIfscCode(),bank.getBankname(),bank.getBalance()),HttpStatus.OK);
	}

	@DeleteMapping("/{bankAccountId}")
	public ResponseEntity<Map<String,String>> deleteBankAccounts(@PathVariable("bankAccountId") long bankAccountId){
		bankAccountService.deleteBankAccount(bankAccountId);
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
	
	@GetMapping("/search")
	public ResponseEntity<List<BankAccountDto>> getBankAccountsByQuery(@Param("query") String query){
		List<BankAccountDto> banks = bankAccountService.getAllBankAccountsByQuery(query).stream().map((b)->new BankAccountDto(b.getBankAccountId(),b.getAccountNo(),b.getIfscCode(),b.getBankname(),b.getBalance())).collect(Collectors.toList());
		return new ResponseEntity<>(banks,HttpStatus.OK);
	}
}
