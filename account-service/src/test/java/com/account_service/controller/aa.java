package com.account_service.controller;

import com.account_service.dtos.AccountDto;
import com.account_service.entity.Account;
import com.account_service.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        createdAccount.setAccountOpeningDate(new Date());
        createdAccount.setLastActivity(new Date());
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
        account1.setAccountOpeningDate(new Date());
        account1.setLastActivity(new Date());
        account1.setBalance(new BigDecimal(100));

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setCustomerId(456L);
        account2.setAccountOpeningDate(new Date());
        account2.setLastActivity(new Date());
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
