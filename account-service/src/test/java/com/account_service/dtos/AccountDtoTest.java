package com.account_service.dtos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDtoTest {

    @Test
    public void testAccountDtoConstructor() {
        Long customerId = 123L;
        BigDecimal balance = new BigDecimal("1000.50");

        AccountDto accountDto = new AccountDto(customerId, balance);

        assertEquals(customerId, accountDto.getCustomerId());
        assertEquals(balance, accountDto.getBalance());
    }

    @Test
    public void testAccountDtoGettersAndSetters() {
        AccountDto accountDto = new AccountDto();

        Long customerId = 456L;
        BigDecimal balance = new BigDecimal("500.75");

        accountDto.setCustomerId(customerId);
        accountDto.setBalance(balance);

        assertEquals(customerId, accountDto.getCustomerId());
        assertEquals(balance, accountDto.getBalance());
    }

    @Test
    public void testAccountDtoNoArgsConstructor() {
        AccountDto accountDto = new AccountDto();

        assertNull(accountDto.getCustomerId());
        assertNull(accountDto.getBalance());
    }

    @Test
    public void testAccountDtoAllArgsConstructor() {
        Long customerId = 789L;
        BigDecimal balance = new BigDecimal("200.30");

        AccountDto accountDto = new AccountDto(customerId, balance);

        assertEquals(customerId, accountDto.getCustomerId());
        assertEquals(balance, accountDto.getBalance());
    }

    @Test
    public void testAccountDtoToString() {
        Long customerId = 111L;
        BigDecimal balance = new BigDecimal("300.90");

        AccountDto accountDto = new AccountDto(customerId, balance);

        String expectedToString = "AccountDto(customerId=" + customerId + ", balance=" + balance + ")";
        assertEquals(expectedToString, accountDto.toString());
    }
}

