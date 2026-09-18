package com.coforge.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.dtos.BillPaymentRequestDto;
import com.coforge.dtos.BillPaymentResponseDto;
import com.coforge.entities.BillPayment;
import com.coforge.entities.BillType;
import com.coforge.services.BillPaymentService;

@RestController
@RequestMapping("/admin/billPayments")
@CrossOrigin
public class BillPaymentAdminController {
	@Autowired
	BillPaymentService billPaymentService;
	
	@GetMapping("/getAll")
	public ResponseEntity<List<BillPaymentResponseDto>> getAllBillPayments(BillPaymentRequestDto billPaymentRequestDto){		
        List<BillPaymentResponseDto> billPayments = billPaymentService.getAllBillPayments().stream().map((b)->new BillPaymentResponseDto(b.getBillId(),b.getPaymentDate(),b.getAmount(),b.getBillType(),b.getBillData())).collect(Collectors.toList());
		return new ResponseEntity<>(billPayments,HttpStatus.CREATED);
	}
	
	@GetMapping("/get/{billId}")
	public ResponseEntity<BillPaymentResponseDto> getBillPayment(@PathVariable("billId") long billId){
		BillPayment billPayment =  billPaymentService.getBillPaymentByBillId(billId);
        return new ResponseEntity<>(new BillPaymentResponseDto(billPayment.getBillId(), billPayment.getPaymentDate(),billPayment.getAmount(), billPayment.getBillType(), billPayment.getBillData()),HttpStatus.CREATED);
	}
	
	@GetMapping("/getBetween")
	public ResponseEntity<List<BillPaymentResponseDto>> getAllBillPaymentsBetweenDate(@Param("start") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate start,@Param("end") LocalDate end){
		List<BillPaymentResponseDto> billPayments =  billPaymentService.getAllBillPaymentsBetweenPaymentDate(start.atStartOfDay(),end.plusDays(1).atStartOfDay()).stream().map((b)->new BillPaymentResponseDto(b.getBillId(),b.getPaymentDate(),b.getAmount(),b.getBillType(),b.getBillData())).collect(Collectors.toList());
		
		return new ResponseEntity<>(billPayments,HttpStatus.CREATED);
	}
	
	@GetMapping("/getByType/{billType}")
	public ResponseEntity<List<BillPaymentResponseDto>> getBillPaymentByBillType(@PathVariable("billType") BillType billType){
		List<BillPaymentResponseDto> billPayments =  billPaymentService.getAllBillPaymentsByBillType(billType).stream().map((b)->new BillPaymentResponseDto(b.getBillId(),b.getPaymentDate(),b.getAmount(),b.getBillType(),b.getBillData())).collect(Collectors.toList());
		
        return new ResponseEntity<>(billPayments,HttpStatus.CREATED);
	}
	
	@GetMapping("/getBetween/{billType}")
	public ResponseEntity<List<BillPaymentResponseDto>> getAllBillPaymentsBetweenDateByBillType(@PathVariable("billType") BillType billType,@Param("start") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate start,@Param("end") LocalDate end){
		List<BillPaymentResponseDto> billPayments =  billPaymentService.getAllBillPaymentsBetweenPaymentDateAndBillTypeCustomer(start.atStartOfDay(),end.plusDays(1).atStartOfDay(),billType).stream().map((b)->new BillPaymentResponseDto(b.getBillId(),b.getPaymentDate(),b.getAmount(),b.getBillType(),b.getBillData())).collect(Collectors.toList());
		
		return new ResponseEntity<>(billPayments,HttpStatus.CREATED);
	}

	@GetMapping("/search")
	public ResponseEntity<List<BillPaymentResponseDto>> searchBillPayment(@Param("query") String query){
		List<BillPaymentResponseDto> billPayments =  billPaymentService.searchBillPayments(query).stream().map((b)->new BillPaymentResponseDto(b.getBillId(),b.getPaymentDate(),b.getAmount(),b.getBillType(),b.getBillData())).collect(Collectors.toList());
		
		return new ResponseEntity<>(billPayments,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{billId}")
	public ResponseEntity<Map<String,String>> deleteBillPayment(@PathVariable("billId") long billId){
		billPaymentService.deleteBillPayment(billId);
		
		return new ResponseEntity<>(Map.of("message","Bill Deleted Successfully"),HttpStatus.CREATED);
	}
}
