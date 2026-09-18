package com.coforge.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Transaction {
	@Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
  private Long transactionId;
  @NotNull
  String transactionType;
  @NotNull
  String transactionStatus;
  @NotNull
  @Min(value=1,message="Min amount for transaction is 1")
  double transactionAmount;
  @NotNull
  LocalDate transactionDate;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "custId")
  Customer customer;
 
  
  
  String description;


  @Enumerated(EnumType.STRING)
  private TransactionCategory category;

  @Enumerated(EnumType.STRING)
  private TransactionSubCategory subCategory;

  public Transaction(@NotNull String transactionType, @NotNull String transactionStatus, double transactionAmount,
		Customer customer, String description, TransactionCategory category, TransactionSubCategory subCategory) {
	super();
	this.transactionType = transactionType;
	this.transactionStatus = transactionStatus;
	this.transactionAmount = transactionAmount;
	this.customer = customer;
	this.description = description;
	this.category = category;
	this.subCategory = subCategory;
  }



  
  

}