package com.coforge.dtos;

import java.time.LocalDate;

import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
	public class TransactionDto {

private Long transactionId;
private String transactionType;
private String transactionStatus;
private double transactionAmount;

private Long customerId;
private TransactionCategory category;
private TransactionSubCategory subCategory;
private String description;

private LocalDate transactionDate;

	}

