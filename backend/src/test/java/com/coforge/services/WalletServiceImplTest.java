package com.coforge.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.coforge.dtos.CustomerJWTTokenDto;
import com.coforge.entities.Beneficiary;
import com.coforge.entities.Wallet;
import com.coforge.exception.InsufficientBalanceException;
import com.coforge.exception.WalletNotFoundException;
import com.coforge.repositories.WalletRepository;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet testWallet;

    @BeforeEach
    void setUp() {
        testWallet = new Wallet();
        testWallet.setWalletId(1L);
        testWallet.setBalance(new BigDecimal("100.00"));
        testWallet.setBeneficiary(new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("credit should increase wallet balance and save wallet")
    void credit_shouldIncreaseBalance() {
        BigDecimal creditAmount = new BigDecimal("50.00");
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updated = walletService.credit(1L, creditAmount);

        assertNotNull(updated);
        assertEquals(new BigDecimal("150.00"), updated.getBalance());
        verify(walletRepository, times(1)).findById(1L);
        verify(walletRepository, times(1)).save(testWallet);
    }

    @Test
    @DisplayName("credit with invalid wallet ID should throw WalletNotFoundException")
    void credit_withInvalidWalletId_shouldThrow() {
        BigDecimal creditAmount = new BigDecimal("50.00");
        when(walletRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> 
            walletService.credit(999L, creditAmount)
        );

        verify(walletRepository, times(1)).findById(999L);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("debit should decrease wallet balance and save wallet")
    void debit_shouldDecreaseBalance() {
        BigDecimal debitAmount = new BigDecimal("40.00");
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updated = walletService.debit(1L, debitAmount);

        assertNotNull(updated);
        assertEquals(new BigDecimal("60.00"), updated.getBalance());
        verify(walletRepository, times(1)).findById(1L);
        verify(walletRepository, times(1)).save(testWallet);
    }

    @Test
    @DisplayName("debit with insufficient balance should throw InsufficientBalanceException")
    void debit_insufficientBalance_shouldThrow() {
        BigDecimal debitAmount = new BigDecimal("150.00");
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

        assertThrows(InsufficientBalanceException.class, () -> 
            walletService.debit(1L, debitAmount)
        );

        verify(walletRepository, times(1)).findById(1L);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("debit with invalid wallet ID should throw WalletNotFoundException")
    void debit_withInvalidWalletId_shouldThrow() {
        BigDecimal debitAmount = new BigDecimal("20.00");
        when(walletRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> 
            walletService.debit(999L, debitAmount)
        );

        verify(walletRepository, times(1)).findById(999L);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("getBalance should return correct wallet balance")
    void getBalance_shouldReturnCorrectBalance() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

        BigDecimal balance = walletService.getBalance(1L);

        assertEquals(new BigDecimal("100.00"), balance);
        verify(walletRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getBalance with invalid wallet ID should throw WalletNotFoundException")
    void getBalance_notFound_shouldThrow() {
        when(walletRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> 
            walletService.getBalance(999L)
        );

        verify(walletRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("getWallet should return authenticated customer's wallet")
    void getWallet_shouldReturnCustomerWallet() {
        CustomerJWTTokenDto customerDto = new CustomerJWTTokenDto(10L, "John Doe", "9876543210", "john@example.com", "USER");
        Authentication auth = new UsernamePasswordAuthenticationToken(customerDto, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(walletRepository.findWalletByCustomerId(10L)).thenReturn(Optional.of(testWallet));

        Wallet result = walletService.getWallet();

        assertNotNull(result);
        assertEquals(1L, result.getWalletId());
        assertEquals(new BigDecimal("100.00"), result.getBalance());
        verify(walletRepository, times(1)).findWalletByCustomerId(10L);
    }

    @Test
    @DisplayName("getWallet should throw WalletNotFoundException when customer wallet is not found")
    void getWallet_notFound_shouldThrow() {
        CustomerJWTTokenDto customerDto = new CustomerJWTTokenDto(10L, "John Doe", "9876543210", "john@example.com", "USER");
        Authentication auth = new UsernamePasswordAuthenticationToken(customerDto, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(walletRepository.findWalletByCustomerId(10L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> 
            walletService.getWallet()
        );

        verify(walletRepository, times(1)).findWalletByCustomerId(10L);
    }

    @Test
    @DisplayName("getAllWallets should return list of all wallets")
    void getAllWallets_shouldReturnList() {
        Wallet secondWallet = new Wallet();
        secondWallet.setWalletId(2L);
        secondWallet.setBalance(new BigDecimal("200.00"));

        when(walletRepository.findAll()).thenReturn(List.of(testWallet, secondWallet));

        List<Wallet> wallets = walletService.getAllWallets();

        assertNotNull(wallets);
        assertEquals(2, wallets.size());
        assertEquals(1L, wallets.get(0).getWalletId());
        assertEquals(2L, wallets.get(1).getWalletId());
        verify(walletRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("createWallet should set initial balance and save wallet")
    void createWallet_shouldSaveAndReturnWallet() {
        BigDecimal initialBalance = new BigDecimal("50.00");
        Wallet createdWallet = new Wallet();
        createdWallet.setWalletId(5L);
        createdWallet.setBalance(initialBalance);

        when(walletRepository.save(any(Wallet.class))).thenReturn(createdWallet);

        Wallet result = walletService.createWallet(initialBalance);

        assertNotNull(result);
        assertEquals(5L, result.getWalletId());
        assertEquals(initialBalance, result.getBalance());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("getWalletById should return wallet when exists")
    void getWalletById_shouldReturnWallet() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

        Wallet result = walletService.getWalletById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getWalletId());
        verify(walletRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("searchWallet should return matching wallets")
    void searchWallet_shouldReturnMatchingWallets() {
        when(walletRepository.searchWallet("test")).thenReturn(List.of(testWallet));

        List<Wallet> results = walletService.searchWallet("test");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getWalletId());
        verify(walletRepository, times(1)).searchWallet("test");
    }
}
