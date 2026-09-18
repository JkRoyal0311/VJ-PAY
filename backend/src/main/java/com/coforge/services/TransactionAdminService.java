package com.coforge.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coforge.daos.TransactionDao;
import com.coforge.dtos.TransactionDto;
import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
import com.coforge.repositories.TransactionRepository;

@Service
public class TransactionAdminService {

	

	    @Autowired
	    private TransactionDao transactionDao;
	    
	    @Autowired
	    private TransactionRepository transactionRepository;

	    /* ---------------- DTO MAPPER ---------------- */

	    private TransactionDto toDto(Transaction t) {
	        TransactionDto dto = new TransactionDto();

	        dto.setTransactionId(t.getTransactionId());
	        dto.setTransactionType(t.getTransactionType());
	        dto.setTransactionStatus(t.getTransactionStatus());
	        dto.setTransactionAmount(t.getTransactionAmount());
	        dto.setTransactionDate(t.getTransactionDate());
	        dto.setCustomerId(t.getCustomer().getCustId());
	        dto.setDescription(t.getDescription());
	        dto.setCategory(t.getCategory());
	        dto.setSubCategory(t.getSubCategory());

	        return dto;
	    }

	    private List<TransactionDto> toDtoList(List<Transaction> list) {
	        return list.stream().map(this::toDto).toList();
	    }

	    /* -------------------------------------------- */

	   
	    public List<TransactionDto> viewAllTransaction() {
	        return toDtoList(transactionDao.viewAllTransaction());
	    }

	  
	    public List<TransactionDto> viewTransactionByDate(LocalDate from, LocalDate to) {
	        return toDtoList(transactionDao.viewTransactionByDate(from, to));
	    }

	   
	    public List<TransactionDto> getByCategory(TransactionCategory category) {
	        return toDtoList(transactionDao.getByCategory(category));
	    }

	    
	    public List<TransactionDto> getBySubCategory(TransactionSubCategory subCategory) {
	        return toDtoList(transactionDao.getBySubCategory(subCategory));
	    }

	  
	    public List<TransactionDto> viewTransactionByMonth(int month, int year) {
	        return toDtoList(transactionDao.viewTransactionByMonth(month, year));
	    }
	    
		 public List<TransactionDto> getCustomerTransactionsByCategoryAndDate(TransactionCategory category, LocalDate from,
				LocalDate to) {
			return transactionRepository.findByCategoryAndTransactionDateBetween(category, from, to).stream()
	    	        .map(this::toDto)
	    	        .toList();
		 }


		 public List<TransactionDto> getCustomerTransactionsBySubCategoryAndDate(TransactionSubCategory category,
				LocalDate from, LocalDate to) {
			// TODO Auto-generated method stub
			return transactionRepository.findBySubCategoryAndTransactionDateBetween(category, from, to).stream()
	    	        .map(this::toDto)
	    	        .toList();
		 }
		 
		 public List<TransactionDto> searchTransactions(String query){
			 return transactionRepository.searchTransactions(query).stream()
		    	        .map(this::toDto)
		    	        .toList();
		 }
	}

