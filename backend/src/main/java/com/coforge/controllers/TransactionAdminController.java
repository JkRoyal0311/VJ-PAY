package com.coforge.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.dtos.TransactionDto;
import com.coforge.entities.TransactionCategory;
import com.coforge.entities.TransactionSubCategory;
import com.coforge.services.TransactionAdminService;
import com.coforge.services.TransactionService;

@RestController
@RequestMapping("/admin/transactions")
@CrossOrigin
public class TransactionAdminController {
    // ✅ 3. View ALL transactions

    @Autowired
    private TransactionAdminService transactionService;

    @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.viewAllTransaction());
    }

    // ✅ 4. View transactions between two dates
    @GetMapping("/dates")
    public ResponseEntity<List<TransactionDto>> getByDate(
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        LocalDate f = LocalDate.parse(from);
        LocalDate t = LocalDate.parse(to);

        return ResponseEntity.ok(transactionService.viewTransactionByDate(f, t));
    }

    // ✅ 5. View transactions by CATEGORY
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionDto>> getByCategory(
            @PathVariable TransactionCategory category
    ) {
        return ResponseEntity.ok(transactionService.getByCategory(category));
    }
    
    @GetMapping("/category/{category}/dates")
    public ResponseEntity<List<TransactionDto>> getByCategoryAndDate(
    		@PathVariable TransactionCategory category,
    		@RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to
    		) {
    	return ResponseEntity.ok(transactionService.getCustomerTransactionsByCategoryAndDate(category,from,to));
    }

    // ✅ 6. View transactions by SUBCATEGORY
    @GetMapping("/subcategory/{sub}")
    public ResponseEntity<List<TransactionDto>> getBySubCategory(
            @PathVariable TransactionSubCategory sub
    ) {
        return ResponseEntity.ok(transactionService.getBySubCategory(sub));
    }
    
    @GetMapping("/subcategory/{sub}/dates")
    public ResponseEntity<List<TransactionDto>> getByCategoryAndDate(
    		@PathVariable TransactionSubCategory sub,
    		@RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to
    		) {
    	return ResponseEntity.ok(transactionService.getCustomerTransactionsBySubCategoryAndDate(sub,from,to));
    }

//    // ✅ 7. Customer → View their own transactions by CATEGORY
//    @GetMapping("/my/category/{category}")
//    public ResponseEntity<List<TransactionDto>> getMyTransactionsByCategory(
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
//    public ResponseEntity<List<TransactionDto>> getMyTransactionsBySubCategory(
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

	@GetMapping("/search")
	public ResponseEntity<List<TransactionDto>> searchTransactions(@RequestParam("query") String query) {
	    return ResponseEntity.ok(transactionService.searchTransactions(query));
	}
}

