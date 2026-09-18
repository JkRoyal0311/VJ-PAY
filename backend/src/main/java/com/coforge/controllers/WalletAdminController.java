package com.coforge.controllers;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coforge.dtos.CreateWalletDto;
import com.coforge.dtos.WalletAmountDto;
import com.coforge.dtos.WalletBalanceDto;
import com.coforge.dtos.WalletDto;
import com.coforge.entities.Wallet;
import com.coforge.services.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/wallets")
@CrossOrigin
public class WalletAdminController {

    @Autowired
    private WalletService walletService;
    
    @GetMapping
    public ResponseEntity<List<WalletDto>> getAllWallets() {
    	List<WalletDto> wallets = walletService.getAllWallets().stream().map((w)->new WalletDto(w.getWalletId(),w.getBalance(),w.getBeneficiary())).collect(Collectors.toList());
        return new ResponseEntity<>(wallets,HttpStatus.OK);
    }

    @GetMapping("/get/{walletId}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable("walletId") Long walletId) {
    	Wallet w = walletService.getWalletById(walletId);
    	return new ResponseEntity<>(new WalletDto(w.getWalletId(),w.getBalance(),w.getBeneficiary()),HttpStatus.OK);    
	}

    @PostMapping("/{walletId}/credit")
    public ResponseEntity<WalletDto> credit(
            @PathVariable Long walletId,
            @Valid @RequestBody WalletAmountDto dto) {
        Wallet w = walletService.credit(walletId, dto.getAmount());
        return new ResponseEntity<>(new WalletDto(w.getWalletId(), w.getBalance(), w.getBeneficiary()), HttpStatus.OK);
    }

    @PostMapping("/{walletId}/debit")
    public ResponseEntity<WalletDto> debit(
            @PathVariable Long walletId,
            @Valid @RequestBody WalletAmountDto dto) {
        Wallet w = walletService.debit(walletId, dto.getAmount());
        return new ResponseEntity<>(new WalletDto(w.getWalletId(), w.getBalance(), w.getBeneficiary()), HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<WalletDto>> getWallet(@Param("query") String query) {
    	List<WalletDto> wallets = walletService.searchWallet(query).stream().map((w)->new WalletDto(w.getWalletId(),w.getBalance(),w.getBeneficiary())).collect(Collectors.toList());;
    	return new ResponseEntity<>(wallets,HttpStatus.OK);    
	}
}