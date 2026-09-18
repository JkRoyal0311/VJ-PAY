package com.coforge.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.coforge.dtos.CustomerJWTTokenDto;
import com.coforge.entities.Beneficiary;
import com.coforge.entities.Wallet;
import com.coforge.exception.InsufficientBalanceException;
import com.coforge.exception.WalletNotFoundException;
import com.coforge.repositories.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;
    
//    @Autowired
//    CustomerService customerService;

    @Override
    public Wallet createWallet(BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setBalance(balance);
        return walletRepository.save(wallet);
    }

    @Override
    public BigDecimal getBalance(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"))
                .getBalance();
    }

    @Override
    public Wallet credit(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }

//    @Override
//    public Wallet topUpWallet(BigDecimal amount,long bankAccountId) {
//    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		CustomerJWTTokenDto customerDto = (CustomerJWTTokenDto) auth.getPrincipal();
//		Wallet wallet = getWalletByCustomerId(customerDto.getCustId());
//    	wallet.setBalance(wallet.getBalance().add(amount));
//    	return walletRepository.save(wallet);
//    }

    @Override
    public Wallet debit(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        return walletRepository.save(wallet);
    }

	@Override
	public Wallet addBeneficiary(long walletId, Beneficiary beneficiary) {
		System.out.println("Hhh");
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
		System.out.println("Wallet got from repo");
//		System.out.println(wallet);
		wallet.addBenificiary(beneficiary);
		walletRepository.save(wallet);
//		System.out.println(wallet);
		return wallet;
	}

	@Override
	public Wallet getWallet() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customer = (CustomerJWTTokenDto) auth.getPrincipal();
		
		Wallet wallet =  walletRepository.findWalletByCustomerId(customer.getCustId()).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
		
		return wallet;
	}

	@Override
	public List<Beneficiary> getWalletBeneficiaries(long walletId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerJWTTokenDto customer = (CustomerJWTTokenDto) auth.getPrincipal();
		
		Wallet wallet =  walletRepository.findWalletByCustomerId(customer.getCustId()).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
		
		return wallet.getBeneficiary();
	}

	@Override
	public Wallet getWalletByCustomerId(long custId) {
		
		Wallet wallet =  walletRepository.findWalletByCustomerId(custId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
		
		return wallet;

	}

	@Override
	public List<Wallet> getAllWallets() {
		return walletRepository.findAll();
	}

	@Override
	public Wallet getWalletById(long walletId) {
		return walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
	}

	@Override
	public List<Wallet> searchWallet(String query) {
		return walletRepository.searchWallet(query);
	}
}