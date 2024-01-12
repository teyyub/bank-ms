package com.account_service.services;

import com.account_service.dtos.AccountDto;
import com.account_service.entity.Account;
import com.account_service.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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
        newAccount.setAccountOpeningDate(new Date());
        newAccount.setLastActivity(new Date());
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
