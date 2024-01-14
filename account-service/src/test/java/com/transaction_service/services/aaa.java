package com.transaction_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.transaction_service.dtos.AccountDto;
import com.transaction_service.entity.Account;
import com.transaction_service.repositories.AccountRepository;

//by default h2 de yaradir istesek propertiden oxuya bilerik
//@TestPropertySource(locations = "classpath:test.properties")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateAccount() {
        // Given
        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(123L);

        Account newAccount = new Account();
        newAccount.setAccountId(1L);
        newAccount.setCustomerId(accountDto.getCustomerId());
        newAccount.setAccountOpeningDate(LocalDateTime.now());
        newAccount.setLastActivity(LocalDateTime.now());
        newAccount.setBalance(new BigDecimal(100));

        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        // When
        Account createdAccount = accountService.create(accountDto);

        // Then
        assertEquals(newAccount.getCustomerId(), createdAccount.getCustomerId());
        assertEquals(newAccount.getAccountOpeningDate(), createdAccount.getAccountOpeningDate());
        assertEquals(newAccount.getLastActivity(), createdAccount.getLastActivity());
        assertEquals(newAccount.getBalance(), createdAccount.getBalance());
    }

    @Test
    void testGetAccounts() {
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

        when(accountRepository.findAll()).thenReturn(accounts);

        // When
        List<Account> retrievedAccounts = accountService.getAccounts();

        // Then
        assertEquals(accounts.size(), retrievedAccounts.size());
        assertEquals(accounts.get(0).getCustomerId(), retrievedAccounts.get(0).getCustomerId());
        assertEquals(accounts.get(1).getCustomerId(), retrievedAccounts.get(1).getCustomerId());
    }

    // Add more test methods for other service methods...
}
