package com.coforge.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.coforge.daos.BillPaymentDao;
import com.coforge.dtos.BillPaymentRequestDto;
import com.coforge.dtos.CustomerJWTTokenDto;
import com.coforge.dtos.ElectricityBillPaymentDto;
import com.coforge.dtos.GasCylinderBookingBillPaymentDto;
import com.coforge.dtos.MobileRechargeBillPaymentDto;
import com.coforge.entities.BillPayment;
import com.coforge.entities.BillType;
import com.coforge.entities.Customer;
import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
import com.coforge.entities.Wallet;
import com.coforge.exception.BillPaymentNotFoundException;
import com.coforge.exception.InvalidBillPaymentDataException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillPaymentService implements BillPaymentServiceInterface {
	@Autowired
	BillPaymentDao billPaymentDao;

	@Autowired
	WalletServiceImpl walletService;

	@Autowired
	CustomerService customerService;

	@Autowired
	TransactionService transactionService;
	
	@Override
	public List<BillPayment> getAllBillPayments() {
		return billPaymentDao.getAllBillPayments();
	}

	@Override
	public List<BillPayment> getAllBillPaymentsCustomer() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		
		return billPaymentDao.getAllBillPaymentsByWalletId(wallet.getWalletId());
	}

	@Override
	public BillPayment getBillPaymentByBillId(long billId) {
		return billPaymentDao.getBillPaymentByBillId(billId).orElseThrow();
	}

	@Override
	public BillPayment getBillPaymentByBillIdCustomer(long billId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		
		BillPayment b = billPaymentDao.getBillPaymentByBillIdAndWalletId(billId,wallet.getWalletId()).orElseThrow(()->new BillPaymentNotFoundException("Bill Payment not found"));
		return b;
	}

	@Override
	public BillPayment createBillPayment(BillPaymentRequestDto billPaymentRequestDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Customer customer = customerService.getById(customerDto.getCustId());
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		
		BillPayment billPayment = new BillPayment();
		String description="Bill Payment "+billPaymentRequestDto.getBillType();
		if(billPaymentRequestDto.getBillType().equals(BillType.ELECTRICITY)){
			description+= " -> state: "+billPaymentRequestDto.getBillData().get("state");
			description+= " billerName: "+billPaymentRequestDto.getBillData().get("billerName");
			description+= " accountNumber: "+billPaymentRequestDto.getBillData().get("accountNumber");
		}
		else if(billPaymentRequestDto.getBillType().equals(BillType.GAS_BOOKING)){
			description+= " -> gasProvider: "+billPaymentRequestDto.getBillData().get("gasProvider");
			description+= " customerNumber: "+billPaymentRequestDto.getBillData().get("customerNumber");
		}
		else if(billPaymentRequestDto.getBillType().equals(BillType.MOBILE_RECHARGE)){
			description+= " -> mobileNumber: "+billPaymentRequestDto.getBillData().get("mobileNumber");
			description+= " operator: "+billPaymentRequestDto.getBillData().get("operator");
		}
//		System.out.println(description);
		Transaction trans = new Transaction(
		    "DEBIT",
		    "PENDING",
		    billPaymentRequestDto.getAmount(),
		    customer,
		    description,
		    TransactionCategory.BILL_PAYMENT,
		    mapBillType(billPaymentRequestDto.getBillType()) 
		);

        Transaction transaction = transactionService.addTransaction(trans);
		try {
			billPayment = convertBillPaymentRequestDtoToBillPayment(billPaymentRequestDto);
			billPayment.setWallet(wallet);
			billPayment.setPaymentDate(LocalDateTime.now());
//			System.out.println(billPayment);
			walletService.debit(wallet.getWalletId(), BigDecimal.valueOf(billPaymentRequestDto.getAmount()));
			
			BillPayment b = billPaymentDao.saveBillPayment(billPayment);
			System.out.println(b.getBillId());
			System.out.println(b.getPaymentDate());
			transaction.setTransactionStatus("SUCCESS");
            transactionService.updateTransaction(transaction);
            
			return b;
		}
		catch(Exception e) {
			System.out.println(e);
			transaction.setTransactionStatus("FAILED");
			transactionService.updateTransaction(transaction);
			throw e;
//			return null;
		}
	}



	
	public BillPayment convertBillPaymentRequestDtoToBillPayment(BillPaymentRequestDto billPaymentRequestDto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			BillPayment billPayment = new BillPayment();
			billPayment.setAmount(billPaymentRequestDto.getAmount());
			billPayment.setBillType(billPaymentRequestDto.getBillType());
			if(billPaymentRequestDto.getBillType().equals(BillType.MOBILE_RECHARGE)) {
				MobileRechargeBillPaymentDto mobileDto = objectMapper.convertValue(billPaymentRequestDto.getBillData(),MobileRechargeBillPaymentDto.class);
			}
			else if(billPaymentRequestDto.getBillType().equals(BillType.GAS_BOOKING)) {
				GasCylinderBookingBillPaymentDto gasBookingDto = objectMapper.convertValue(billPaymentRequestDto.getBillData(),GasCylinderBookingBillPaymentDto.class);
			}
			else if(billPaymentRequestDto.getBillType().equals(BillType.ELECTRICITY)) {
				ElectricityBillPaymentDto electicityBilDto = objectMapper.convertValue(billPaymentRequestDto.getBillData(),ElectricityBillPaymentDto.class);
			}
			billPayment.setBillData(billPaymentRequestDto.getBillData());
			return billPayment;
		}
		catch(Exception e) {
			System.out.println(e);
			throw new InvalidBillPaymentDataException("Invalid bill data");
		}
	}
	
	
//	@Override
//	public BillPayment updateBillPayment(BillPayment billPayment) {
//		getBillPaymentByBillId(billPayment.getBillId());
//		BillPayment b = billPaymentDao.saveBillPayment(billPayment);
//		return createBillPaymentResponseDtoFromBillPayment(b);
//	}

//	@Override
//	public BillPayment updateBillPaymentCustomer(BillPayment billPayment) {
//		BillPayment b = getBillPaymentByBillId(billPayment.getBillId());
//		BillPayment b = billPaymentDao.saveBillPayment(billPayment);
//		return b;
//	}

	@Override
	public void deleteBillPayment(long billId) {
		getBillPaymentByBillId(billId);
		billPaymentDao.deleteBillPayment(billId);
	}

	@Override
	public void deleteBillPaymentCustomer(long billId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		
		BillPayment b = billPaymentDao.getBillPaymentByBillIdAndWalletId(billId,wallet.getWalletId()).orElseThrow(()->new BillPaymentNotFoundException("Bill Payment not found"));
		
		billPaymentDao.deleteBillPayment(b.getBillId());
	}
//	
////	public BillPaymentResponseDto createBillPaymentResponseDtoFromBillPayment(BillPayment billPayment) {
////		return new BillPaymentResponseDto(billPayment.getBillId(),billPayment.getPaymentDate(),billPayment.getAmount(),billPayment.getBillType(),billPayment.getBillData());
////	}
	private TransactionSubCategory mapBillType(BillType billType) {
	    return switch (billType) {
	        case ELECTRICITY -> TransactionSubCategory.ELECTRICITY;
	        case MOBILE_RECHARGE -> TransactionSubCategory.MOBILE_RECHARGE;
	        case GAS_BOOKING -> TransactionSubCategory.GAS;
	    };
	}

	@Override
	public List<BillPayment> getAllBillPaymentsByBillType(BillType billType) {
		return billPaymentDao.getAllBillPaymentsByType(billType);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsByBillTypeCustomer(BillType billType) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());

		return billPaymentDao.getAllBillPaymentsByBillTypeAndWalletId(billType, wallet.getWalletId());
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDate(LocalDateTime start, LocalDateTime end) {
		return billPaymentDao.getAllBillPaymentsBetweenPaymentDate(start, end);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateCustomer(LocalDateTime start, LocalDateTime end) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		return billPaymentDao.getAllBillPaymentsBetweenPaymentDateAndWalletId(start, end, wallet.getWalletId());
	}

	@Override
	public List<BillPayment> searchBillPayments(String query) {
		return billPaymentDao.searchBillPayments(query);
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillTypeCustomer(LocalDateTime start,
			LocalDateTime end, BillType billtype) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
		Wallet wallet = walletService.getWalletByCustomerId(customerDto.getCustId());
		return billPaymentDao.getAllBillPaymentsBetweenPaymentDateAndBillTypeAndWalletId(start,end,billtype,wallet.getWalletId());
	}

	@Override
	public List<BillPayment> getAllBillPaymentsBetweenPaymentDateAndBillType(LocalDateTime start, LocalDateTime end,
			BillType billtype) {
		return billPaymentDao.getAllBillPaymentsBetweenPaymentDateAndBillType(start,end,billtype);
	}
}
