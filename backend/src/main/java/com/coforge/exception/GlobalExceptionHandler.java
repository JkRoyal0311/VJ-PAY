package com.coforge.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.coforge.security.JwtAuthenticationFilter;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(BillPaymentNotFoundException.class)
	public ResponseEntity<Map<String,String>> handleBillPaymentNotFoundException(BillPaymentNotFoundException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<Map<String,String>> handleInvalidOtpException(InvalidOtpException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Map<String,String>> handleInvalidPasswordException(InvalidPasswordException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidBillPaymentDataException.class)
	public ResponseEntity<Map<String,String>> handleInvalidBillPaymentDataException(InvalidBillPaymentDataException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BankAccountNotFoundException.class)
	public ResponseEntity<Map<String,String>> handleBankAccountNotFoundException(BankAccountNotFoundException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BankAccountInsufficientBalanceException.class)
	public ResponseEntity<Map<String,String>> handleBankAccountInsufficientBalanceException(BankAccountInsufficientBalanceException e){
		logger.error("Error: {}",e.getMessage());
		System.out.println("In the exception");
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomerAlreadyExistsException.class)
	public ResponseEntity<Map<String,String>> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Map<String,String>> handleCustomerNotFoundException(CustomerNotFoundException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<Map<String,String>> handleInsufficientBalanceException(InsufficientBalanceException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(WalletNotFoundException.class)
	public ResponseEntity<Map<String,String>> handleWalletNotFoundException(WalletNotFoundException e){
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BeneficiaryException.class)
	public ResponseEntity<Map<String,String>> handleBeneficiaryNotFoundException(BeneficiaryException e){
		logger.error("Error: {}",e.getMessage());
	 return new ResponseEntity<>(Map.of("message",e.getMessage()) ,HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleTransactionNotFound(TransactionNotFoundException e) {
		logger.error("Error: {}",e.getMessage());
		return new ResponseEntity<>(Map.of("message",e.getMessage()) ,HttpStatus.NOT_FOUND);
	}
}