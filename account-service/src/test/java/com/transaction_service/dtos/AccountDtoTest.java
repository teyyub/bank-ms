package com.transaction_service.dtos;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class AccountDtoTest {

    @Test
    public void testAccountDtoConstructor() {
        Long customerId = 123L;
        BigDecimal balance = new BigDecimal("1000.50");
        String gsmNumber= "1234";

        AccountDto accountDto = new AccountDto(customerId, gsmNumber,balance);

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
        String gsmNumber ="123";
        AccountDto accountDto = new AccountDto(customerId, gsmNumber,balance);

        assertEquals(customerId, accountDto.getCustomerId());
        assertEquals(balance, accountDto.getBalance());
    }

    @Test
    public void testAccountDtoToString() {
        Long customerId = 111L;
        BigDecimal balance = new BigDecimal("300.90");
        String gsmNumber ="1234";
        AccountDto accountDto = new AccountDto(customerId, gsmNumber,balance);

        String expectedToString = "AccountDto(customerId=" + customerId + ", balance=" + balance + ")";
        assertEquals(expectedToString, accountDto.toString());
    }
}

