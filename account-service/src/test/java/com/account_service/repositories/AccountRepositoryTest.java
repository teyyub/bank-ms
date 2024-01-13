package com.account_service.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.account_service.entity.Account;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFindByCustomerId() {
        // Given
        Long customerId = 123L;
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setBalance(new BigDecimal("1000.50"));
        account.setAccountType("Savings");
        account.setAccountOpeningDate(new Date());
        account.setLastActivity(new Date());

        // When
        accountRepository.save(account);
        List<Account> foundAccounts = accountRepository.findByCustomerId(customerId);

        // Then
        assertNotNull(foundAccounts);
        assertEquals(1, foundAccounts.size());
        assertEquals(customerId, foundAccounts.get(0).getCustomerId());
        assertEquals(account.getBalance(), foundAccounts.get(0).getBalance());
        assertEquals(account.getAccountType(), foundAccounts.get(0).getAccountType());
        assertEquals(account.getAccountOpeningDate(), foundAccounts.get(0).getAccountOpeningDate());
        assertEquals(account.getLastActivity(), foundAccounts.get(0).getLastActivity());
    }
}

