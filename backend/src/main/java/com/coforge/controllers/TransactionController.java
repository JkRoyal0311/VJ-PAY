package com.coforge.controllers;
 
 
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import com.coforge.dtos.TransactionDto;
import com.coforge.entities.Transaction;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
 
import com.coforge.services.TransactionService;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
 
@RestController
@RequestMapping("/auth/transactions")
@CrossOrigin
public class TransactionController {
 
    @Autowired
    private TransactionService transactionService;
 
    // ✅ 1. Add Transaction
    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction tx) {
        return ResponseEntity.ok(transactionService.addTransaction(tx));
    }
 
    // ✅ 2. Update Transaction (SUCCESS / FAILED)
    @PutMapping
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction tx) {
        return ResponseEntity.ok(transactionService.updateTransaction(tx));
    }
 
    // ✅ 3. View ALL transactions
    @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.viewAllTransactionCustomer());
    }
 
    // ✅ 4. View transactions between two dates
    @GetMapping("/dates")
    public ResponseEntity<List<TransactionDto>> getByDate(
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        LocalDate f = LocalDate.parse(from);
        LocalDate t = LocalDate.parse(to);
 
//        return ResponseEntity.ok(transactionService.viewTransactionByDate(f, t));
        return ResponseEntity.ok(transactionService.viewTransactionByDateCustomer(f, t));
    }
 
    // ✅ 5. View transactions by CATEGORY
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionDto>> getByCategory(
            @PathVariable TransactionCategory category
    ) {
        return ResponseEntity.ok(transactionService.getByCategoryCustomer(category));
    }

    @GetMapping("/category/{category}/dates")
    public ResponseEntity<List<TransactionDto>> getByCategoryAndDate(
    		@PathVariable TransactionCategory category,
    		@RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to
    		) {
    	return ResponseEntity.ok(transactionService.getCustomerTransactionsByCategoryAndDateCustomer(category,from,to));
    }
 
    // ✅ 6. View transactions by SUBCATEGORY
    @GetMapping("/subcategory/{sub}")
    public ResponseEntity<List<TransactionDto>> getBySubCategory(
            @PathVariable TransactionSubCategory sub
    ) {
        return ResponseEntity.ok(transactionService.getBySubCategoryCustomer(sub));
    }
    
    @GetMapping("/subcategory/{sub}/dates")
    public ResponseEntity<List<TransactionDto>> getByCategoryAndDate(
    		@PathVariable TransactionSubCategory sub,
    		@RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to
    		) {
    	return ResponseEntity.ok(transactionService.getCustomerTransactionsBySubCategoryAndDateCustomer(sub,from,to));
    }
 
//    // ✅ 7. Customer → View their own transactions by CATEGORY
//    @GetMapping("/my/category/{category}")
//    public ResponseEntity<List<Transaction>> getMyTransactionsByCategory(
//            @PathVariable TransactionCategory category
//    ) {
//       
//        return ResponseEntity.ok(
//                transactionService.getCustomerTransactionsByCategory(customer.getCustId(), category)
//        );
//    }
//
//    // ✅ 8. Customer → View their own transactions by SUBCATEGORY
//    @GetMapping("/my/subcategory/{sub}")
//    public ResponseEntity<List<Transaction>> getMyTransactionsBySubCategory(
//            @PathVariable TransactionSubCategory sub
//    ) {
//       
//        return ResponseEntity.ok(
//                transactionService.getCustomerTransactionsBySubCategory(customer.getCustId(), sub)
//        );
//    }
 
@GetMapping("/month")
    public ResponseEntity<List<TransactionDto>> getByMonth(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(transactionService.viewTransactionByMonth(month, year));
    }
@DeleteMapping("/{id}")
public ResponseEntity<Map<String,String>> deleteTransaction(@PathVariable Long id) {
    String msg = transactionService.deleteTransaction(id);
    return ResponseEntity.ok(Map.of("message",msg));
}
}