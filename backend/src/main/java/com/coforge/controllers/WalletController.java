package com.coforge.controllers;


import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/auth/wallets")
@CrossOrigin
public class WalletController {

    @Autowired
    private WalletService walletService;
    
    @GetMapping
    public ResponseEntity<WalletDto> getWallet() {
        Wallet wallet = walletService.getWallet();
        return  new ResponseEntity<>(new WalletDto(wallet.getWalletId(),wallet.getBalance(),wallet.getBeneficiary()),HttpStatus.CREATED);
    }

//    @PostMapping
//    public Wallet createWallet(@Valid @RequestBody CreateWalletDto dto) {
//        return walletService.createWallet(dto.getBalance());
//    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<WalletBalanceDto> getBalance(@PathVariable Long walletId) {
        return new ResponseEntity<>(new WalletBalanceDto(walletService.getBalance(walletId)),HttpStatus.OK);
    }

//    @PostMapping("/{walletId}/credit")
//    public Wallet credit(
//            @PathVariable Long walletId,
//            @Valid @RequestBody WalletAmountDto dto) {
//        return walletService.credit(walletId, dto.getAmount());
//    }
//
//    @PostMapping("/{walletId}/debit")
//    public Wallet debit(
//            @PathVariable Long walletId,
//            @Valid @RequestBody WalletAmountDto dto) {
//        return walletService.debit(walletId, dto.getAmount());
//    }
}