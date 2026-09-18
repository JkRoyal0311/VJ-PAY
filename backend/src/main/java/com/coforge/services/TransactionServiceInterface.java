package com.coforge.services;

import java.time.LocalDate;
import java.util.List;

import com.coforge.dtos.TransactionDto;
import com.coforge.entities.Transaction;


import java.time.LocalDate;
import java.util.List;

import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;

public interface TransactionServiceInterface {

    // Add Transaction
    public Transaction addTransaction(Transaction tran);

    // Update Transaction (SUCCESS / FAILED)
    public Transaction updateTransaction(Transaction tran);

    // View ALL Transactions
    public List<TransactionDto> viewAllTransaction();

    // View Transactions by Date Range
    public List<TransactionDto> viewTransactionByDate(LocalDate from, LocalDate to);

    // View by Category (BENEFICIARY, BILL_PAYMENT, TOP_UP)
    public List<TransactionDto> getByCategory(TransactionCategory category);

    //View by SubCategory (ELECTRICITY, GAS, MOBILE_RECHARGE, etc.)
    public List<TransactionDto> getBySubCategory(TransactionSubCategory subCategory);

    // View Customer's Transactions by Category
    public List<TransactionDto> getCustomerTransactionsByCategory(Long custId, TransactionCategory category);

    public List<TransactionDto> getCustomerTransactionsByCategoryAndDateCustomer(TransactionCategory category,LocalDate from, LocalDate to);
    public List<TransactionDto> getCustomerTransactionsBySubCategoryAndDateCustomer(TransactionSubCategory category,LocalDate from, LocalDate to);

    //View Customer's Transactions by SubCategory
    public List<TransactionDto> getCustomerTransactionsBySubCategory(Long custId, TransactionSubCategory subCategory);

	List<TransactionDto> viewTransactionByMonth(int month, int year);

	String deleteTransaction(Long txId);

	List<TransactionDto> getBySubCategoryCustomer(TransactionSubCategory subCategory);

	List<TransactionDto> getByCategoryCustomer(TransactionCategory category);

	List<TransactionDto> viewTransactionByDateCustomer(LocalDate from, LocalDate to);

}