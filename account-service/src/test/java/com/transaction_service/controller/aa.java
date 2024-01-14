package com.transaction_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction_service.dtos.AccountDto;
import com.transaction_service.entity.Account;
import com.transaction_service.services.AccountService;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void testCreateAccount() throws Exception {
        // Given
        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(123L);

        Account createdAccount = new Account();
        createdAccount.setAccountId(1L);
        createdAccount.setCustomerId(accountDto.getCustomerId());
        createdAccount.setAccountOpeningDate(LocalDateTime.now());
        createdAccount.setLastActivity(LocalDateTime.now());
        createdAccount.setBalance(new BigDecimal(100));

        when(accountService.create(any(AccountDto.class))).thenReturn(createdAccount);

        // When/Then
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(accountDto.getCustomerId()))
                .andExpect(jsonPath("$.balance").value(createdAccount.getBalance().intValue()));
    }

    @Test
    void testGetAccounts() throws Exception {
        // Given
        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setCustomerId(123L);
        account1.setAccountOpeningDate(LocalDateTime.now());
        account1.setLastActivity(LocalDateTime.now());
        account1.setBalance(new BigDecimal(100));

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setCustomerId(456L);
        account2.setAccountOpeningDate(LocalDateTime.now());
        account2.setLastActivity(LocalDateTime.now());
        account2.setBalance(new BigDecimal(200));

        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountService.getAccounts()).thenReturn(accounts);

        // When/Then
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(account1.getCustomerId()))
                .andExpect(jsonPath("$[1].customerId").value(account2.getCustomerId()));
    }

    // Add more test methods for other controller methods...
}
