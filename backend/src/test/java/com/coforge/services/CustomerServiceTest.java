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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coforge.daos.CustomerDao;
import com.coforge.dtos.CustomerDto;
import com.coforge.dtos.CustomerResponseDto;
import com.coforge.entities.Customer;
import com.coforge.entities.Wallet;
import com.coforge.exception.CustomerAlreadyExistsException;
import com.coforge.exception.CustomerNotFoundException;
import com.coforge.repositories.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletServiceImpl walletService;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(1L);
        testCustomer.setCustName("John Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setMobileNumber("9876543210");
        testCustomer.setPwd("Secret@123");
    }

    @Test
    @DisplayName("login with valid credentials should return customer")
    void login_withValidCredentials_shouldReturnCustomer() {
        when(customerDao.findByEmailAndPwd("john@example.com", "Secret@123"))
                .thenReturn(Optional.of(testCustomer));

        Customer result = customerService.login("john@example.com", "Secret@123");

        assertNotNull(result);
        assertEquals(1L, result.getCustId());
        assertEquals("john@example.com", result.getEmail());
        verify(customerDao, times(1)).findByEmailAndPwd("john@example.com", "Secret@123");
    }

    @Test
    @DisplayName("login with invalid credentials should throw CustomerNotFoundException")
    void login_withInvalidCredentials_shouldThrowException() {
        when(customerDao.findByEmailAndPwd("john@example.com", "wrongPassword"))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> 
            customerService.login("john@example.com", "wrongPassword")
        );

        verify(customerDao, times(1)).findByEmailAndPwd("john@example.com", "wrongPassword");
    }

    @Test
    @DisplayName("saveCustomer should create wallet, associate with customer, and return saved customer")
    void saveCustomer_shouldSaveAndReturnCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setCustName("Alice Smith");
        newCustomer.setEmail("alice@example.com");
        newCustomer.setMobileNumber("9876543211");
        newCustomer.setPwd("Password@123");

        Wallet newWallet = new Wallet();
        newWallet.setWalletId(10L);
        newWallet.setBalance(BigDecimal.ZERO);

        when(customerDao.findByEmailOrMobileNumber("alice@example.com", "9876543211"))
                .thenReturn(Collections.emptyList());
        when(walletService.createWallet(BigDecimal.ZERO)).thenReturn(newWallet);
        when(customerDao.saveCustomer(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer saved = customerService.saveCustomer(newCustomer);

        assertNotNull(saved);
        assertEquals("alice@example.com", saved.getEmail());
        assertNotNull(saved.getWallet());
        assertEquals(10L, saved.getWallet().getWalletId());
        assertEquals(BigDecimal.ZERO, saved.getWallet().getBalance());
        verify(customerDao, times(1)).findByEmailOrMobileNumber("alice@example.com", "9876543211");
        verify(walletService, times(1)).createWallet(BigDecimal.ZERO);
        verify(customerDao, times(1)).saveCustomer(newCustomer);
    }

    @Test
    @DisplayName("saveCustomer should throw CustomerAlreadyExistsException when customer exists")
    void saveCustomer_alreadyExists_shouldThrow() {
        when(customerDao.findByEmailOrMobileNumber("john@example.com", "9876543210"))
                .thenReturn(List.of(testCustomer));

        assertThrows(CustomerAlreadyExistsException.class, () -> 
            customerService.saveCustomer(testCustomer)
        );

        verify(customerDao, times(1)).findByEmailOrMobileNumber("john@example.com", "9876543210");
        verify(walletService, never()).createWallet(any());
        verify(customerDao, never()).saveCustomer(any());
    }

    @Test
    @DisplayName("getById should return customer when customer exists")
    void getById_shouldReturnCustomer() {
        when(customerDao.getById(1L)).thenReturn(Optional.of(testCustomer));

        Customer result = customerService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCustId());
        assertEquals("John Doe", result.getCustName());
        verify(customerDao, times(1)).getById(1L);
    }

    @Test
    @DisplayName("getById when customer not found should throw CustomerNotFoundException")
    void getById_notFound_shouldThrow() {
        when(customerDao.getById(999L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> 
            customerService.getById(999L)
        );

        assertEquals("Customer Not Found999", exception.getMessage());
        verify(customerDao, times(1)).getById(999L);
    }

    @Test
    @DisplayName("deleteCustomer should delete transactions and customer by ID")
    void deleteCustomer_shouldDelete() {
        customerService.deleteCustomer(1L);

        verify(transactionRepository, times(1)).deleteByCustomerCustId(1L);
        verify(customerDao, times(1)).deleteCustomer(1L);
    }

    @Test
    @DisplayName("getAllCustomer should return list of customer response DTOs")
    void getAllCustomer_shouldReturnCustomerList() {
        when(customerDao.getAllCustomer()).thenReturn(List.of(testCustomer));

        List<CustomerResponseDto> result = customerService.getAllCustomer();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getCustName());
        assertEquals("john@example.com", result.get(0).getEmail());
        verify(customerDao, times(1)).getAllCustomer();
    }

    @Test
    @DisplayName("getCustomerDtoById should return customer DTO with wallet details")
    void getCustomerDtoById_shouldReturnCustomerDto() {
        Wallet wallet = new Wallet();
        wallet.setWalletId(5L);
        wallet.setBalance(new BigDecimal("500.00"));
        wallet.setBeneficiary(new ArrayList<>());

        when(customerDao.getById(1L)).thenReturn(Optional.of(testCustomer));
        when(walletService.getWalletByCustomerId(1L)).thenReturn(wallet);

        CustomerDto dto = customerService.getCustomerDtoById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getCustId());
        assertEquals("John Doe", dto.getCustName());
        assertNotNull(dto.getWallet());
        assertEquals(5L, dto.getWallet().getWalletId());
        assertEquals(new BigDecimal("500.00"), dto.getWallet().getBalance());
        verify(customerDao, times(1)).getById(1L);
        verify(walletService, times(1)).getWalletByCustomerId(1L);
    }

    @Test
    @DisplayName("loginAdmin with valid credentials should return admin DTO")
    void loginAdmin_withValidCredentials_shouldReturnAdminDto() {
        CustomerResponseDto admin = customerService.loginAdmin("admin@mail.com", "admin@123");

        assertNotNull(admin);
        assertEquals("ADMIN", admin.getCustName());
        assertEquals("admin@mail.com", admin.getEmail());
    }

    @Test
    @DisplayName("loginAdmin with invalid credentials should throw CustomerNotFoundException")
    void loginAdmin_withInvalidCredentials_shouldThrow() {
        assertThrows(CustomerNotFoundException.class, () -> 
            customerService.loginAdmin("admin@mail.com", "wrongPass")
        );
    }
}
