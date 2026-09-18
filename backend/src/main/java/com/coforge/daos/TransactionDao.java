package com.coforge.daos;
 
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
 
import com.coforge.entities.Customer;
import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
import com.coforge.repositories.CustomerRepository;
import com.coforge.repositories.TransactionRepository;
 
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
 
 
@Repository
public class TransactionDao implements TransactionDaoInterface {
 
    @Autowired
    private CustomerRepository customerRepository;
 
    @Autowired
    private TransactionRepository transactionRepository;
 
    @Override
    public Customer findCustomerById(long id) {
        return customerRepository.findById(id)
                .orElse(null);
    }
 
    @Override
    public Transaction saveTransaction(Transaction tx) {
        return transactionRepository.save(tx);
    }
 
    @Override
    public Transaction findTransactionById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<Transaction> viewAllTransaction(){
        String jpql = "SELECT * FROM Transaction";
        return transactionRepository.findAll();
    }
    public List<Transaction> getByCategory(TransactionCategory category) {
        return transactionRepository.findByCategory(category);
    }
 
    public List<Transaction> getBySubCategory(TransactionSubCategory subCategory) {
        return transactionRepository.findBySubCategory(subCategory);
    }
 
    public List<Transaction> getCustomerTransactionsByCategory(Long custId, TransactionCategory category) {
        return transactionRepository.findByCustomer_CustIdAndCategory(custId, category);
    }
 
    public List<Transaction> getCustomerTransactionsBySubCategory(Long custId, TransactionSubCategory subCategory) {
        return transactionRepository.findByCustomer_CustIdAndSubCategory(custId, subCategory);
    }
 
    @Override
      public List<Transaction> viewTransactionByMonth(int month, int year) {
          return transactionRepository.findByMonthAndYear(month, year);
      }
 
 
    @Override
    public List<Transaction> viewTransactionByDate(LocalDate from, LocalDate to) {
        String jpql = "SELECT * FROM Transaction WHERE transactionDate is BETWEEN ? AND ?";
 
//        return entityManager.createQuery(jpql, Transaction.class)
//                .setParameter("from", from)
//                .setParameter("to", to)
//                .getResultList();
      return transactionRepository.findByTransactionDateBetween(from, to);
    }
 
    public List<Transaction> viewTransactionByDateCustomer(LocalDate from, LocalDate to,long customer) {
    	String jpql = "SELECT * FROM Transaction WHERE transactionDate is BETWEEN ? AND ?";
    	
//        return entityManager.createQuery(jpql, Transaction.class)
//                .setParameter("from", from)
//                .setParameter("to", to)
//                .getResultList();
    	System.out.println(customer);
    	return transactionRepository.findByCustomerCustIdAndTransactionDateBetween(customer,from, to);
    }

public void deleteTransaction(Long txId) {
    transactionRepository.deleteById(txId);
}

}