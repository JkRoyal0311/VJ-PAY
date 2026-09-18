package com.coforge.exception;

public class BankAccountInsufficientBalanceException extends RuntimeException {

	public BankAccountInsufficientBalanceException(String msg) {
		super(msg);
	}
	
}
