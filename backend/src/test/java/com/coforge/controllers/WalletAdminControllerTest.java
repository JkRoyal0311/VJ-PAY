package com.coforge.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.coforge.dtos.WalletAmountDto;
import com.coforge.entities.Wallet;
import com.coforge.security.JwtAuthenticationFilter;
import com.coforge.security.JwtUtil;
import com.coforge.services.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = WalletAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class WalletAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Wallet wallet1;
    private Wallet wallet2;

    @BeforeEach
    void setUp() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        wallet1 = new Wallet();
        wallet1.setWalletId(1L);
        wallet1.setBalance(new BigDecimal("100.00"));
        wallet1.setBeneficiary(new ArrayList<>());

        wallet2 = new Wallet();
        wallet2.setWalletId(2L);
        wallet2.setBalance(new BigDecimal("200.00"));
        wallet2.setBeneficiary(new ArrayList<>());
    }

    @Test
    @DisplayName("GET /admin/wallets should return list of all wallets")
    void getAllWallets_shouldReturnList() throws Exception {
        when(walletService.getAllWallets()).thenReturn(List.of(wallet1, wallet2));

        mockMvc.perform(get("/admin/wallets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].walletId").value(1))
                .andExpect(jsonPath("$[0].balance").value(100.00))
                .andExpect(jsonPath("$[1].walletId").value(2))
                .andExpect(jsonPath("$[1].balance").value(200.00));

        verify(walletService, times(1)).getAllWallets();
    }

    @Test
    @DisplayName("GET /admin/wallets/get/{walletId} should return single wallet")
    void getWallet_shouldReturnSingleWallet() throws Exception {
        when(walletService.getWalletById(1L)).thenReturn(wallet1);

        mockMvc.perform(get("/admin/wallets/get/{walletId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(1))
                .andExpect(jsonPath("$.balance").value(100.00));

        verify(walletService, times(1)).getWalletById(1L);
    }

    @Test
    @DisplayName("POST /admin/wallets/{walletId}/credit should return updated wallet")
    void credit_shouldReturnUpdatedWallet() throws Exception {
        WalletAmountDto dto = new WalletAmountDto();
        dto.setAmount(new BigDecimal("50.00"));

        Wallet updatedWallet = new Wallet();
        updatedWallet.setWalletId(1L);
        updatedWallet.setBalance(new BigDecimal("150.00"));
        updatedWallet.setBeneficiary(new ArrayList<>());

        when(walletService.credit(eq(1L), any(BigDecimal.class))).thenReturn(updatedWallet);

        mockMvc.perform(post("/admin/wallets/{walletId}/credit", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(1))
                .andExpect(jsonPath("$.balance").value(150.00));

        verify(walletService, times(1)).credit(eq(1L), any(BigDecimal.class));
    }

    @Test
    @DisplayName("POST /admin/wallets/{walletId}/debit should return updated wallet")
    void debit_shouldReturnUpdatedWallet() throws Exception {
        WalletAmountDto dto = new WalletAmountDto();
        dto.setAmount(new BigDecimal("40.00"));

        Wallet updatedWallet = new Wallet();
        updatedWallet.setWalletId(1L);
        updatedWallet.setBalance(new BigDecimal("60.00"));
        updatedWallet.setBeneficiary(new ArrayList<>());

        when(walletService.debit(eq(1L), any(BigDecimal.class))).thenReturn(updatedWallet);

        mockMvc.perform(post("/admin/wallets/{walletId}/debit", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(1))
                .andExpect(jsonPath("$.balance").value(60.00));

        verify(walletService, times(1)).debit(eq(1L), any(BigDecimal.class));
    }

    @Test
    @DisplayName("GET /admin/wallets/search should return filtered wallets")
    void search_shouldReturnFilteredWallets() throws Exception {
        when(walletService.searchWallet("John")).thenReturn(List.of(wallet1));

        mockMvc.perform(get("/admin/wallets/search")
                .param("query", "John")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].walletId").value(1))
                .andExpect(jsonPath("$[0].balance").value(100.00));

        verify(walletService, times(1)).searchWallet("John");
    }

    @Test
    @DisplayName("POST /admin/wallets/{walletId}/credit with negative amount should return 400 Bad Request")
    void credit_withInvalidAmount_shouldReturnBadRequest() throws Exception {
        WalletAmountDto dto = new WalletAmountDto();
        dto.setAmount(new BigDecimal("-10.00"));

        mockMvc.perform(post("/admin/wallets/{walletId}/credit", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /admin/wallets/{walletId}/debit with null amount should return 400 Bad Request")
    void debit_withNullAmount_shouldReturnBadRequest() throws Exception {
        WalletAmountDto dto = new WalletAmountDto();
        dto.setAmount(null);

        mockMvc.perform(post("/admin/wallets/{walletId}/debit", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
