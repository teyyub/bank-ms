package com.transaction_service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.transaction_service.config.AccountProducer;
import com.transaction_service.config.TransactionProducer;
import com.transaction_service.dtos.AccountResponseDto;
import com.transaction_service.entity.Account;
import com.transaction_service.entity.Transaction;
import com.transaction_service.entity.TransactionType;
import com.transaction_service.exceptions.ResourceNotFoundException;
import com.transaction_service.repositories.AccountRepository;
import com.transaction_service.repositories.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService   {

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    @Value("${transaction.amqp.queue}")
    private String transactionQueue;

    @Value("${account.amqp.queue.topup}")
    private String accountTopUp;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AccountProducer accountProducer;
    private Logger logger = LoggerFactory.getLogger(AccountService.class);


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionProducer transactionProducer;


//    public Account create(AccountDto accountDto) {
//        Account account =toAccount(new Account(),accountDto);
//        log.info("account entity " +account);
//        return accountRepository.save(account);
//    }


//    public List<Account> getAccounts() {
//        return accountRepository.findAll();
//    }


//    public Account getAccount(Long id) {
//        // Getting Accounts from ACCOUNT SERVICE
//            Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found try again with correct details !!"));
//
//            // Getting customers from USER SERVICE
//
////            Customer customer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/" + account.getCustomerId(), Customer.class);
//            Customer customer = restTemplate.getForObject(customerServiceUrl + account.getCustomerId(), Customer.class);
//
////            account.setCustomer(customer);
//
//            return account;
//
//    }

//    public List<Account> getAccountByCustomerId(Long customerId) {
//        return accountRepository.findByCustomerId(customerId);
//    }

//    public Account updateAccount(Long id, Account account) {
//
//        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found  try again with correct details!!"));
//        newAccount.setAccountType(account.getAccountType());
//        newAccount.setLastActivity(LocalDateTime.now());
//        return accountRepository.save(newAccount);
//    }

    @Transactional
    public Transaction addBalance(Long id, int amount, Long customerId) {
         Account newAccount = accountApiCall(id);
        if (newAccount == null) {
            throw new ResourceNotFoundException("Account with given id not found try again with correct details !!");
        }

        BigDecimal amountToAdd = BigDecimal.valueOf(amount);
        if (amountToAdd.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero for a top-up.");
        }
                BigDecimal newBalance = newAccount.getBalance().add(amountToAdd);
                newAccount.setBalance(newBalance);
                newAccount.setLastActivity(LocalDateTime.now());

                Transaction transaction = new Transaction();
                transaction.setAccountId(newAccount.getAccountId());
                transaction.setTransactionType(TransactionType.TOP_UP);
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setAmount(amountToAdd);
                transactionRepository.save(transaction);

                accountProducer.sendTo(accountTopUp,toAccountResponseDto(newAccount));
                transactionProducer.sendTo(transactionQueue,transaction);
                return transaction;

    }

    private AccountResponseDto toAccountResponseDto(Account newAccount) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setAccountId(newAccount.getAccountId());
        dto.setBalance(newAccount.getBalance());
        return dto;
    }

    public Transaction refundBalance(Long id, int amount, Long customerId) {

        Account updatedAccount = accountApiCall(id);
        if (updatedAccount == null) {
                throw new ResourceNotFoundException("Account with given id not found try again with correct details !!");
            }
            BigDecimal currentBalance = updatedAccount.getBalance();
            BigDecimal refundAmount = BigDecimal.valueOf(amount);

                if(currentBalance.compareTo(refundAmount)<0){
                    throw new RuntimeException("Insufficient amount.This operation is not possible");
                }
                BigDecimal newBalance = currentBalance.add(refundAmount);
        updatedAccount.setBalance(newBalance);
        updatedAccount.setLastActivity(LocalDateTime.now());

        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccountId(updatedAccount.getAccountId());
        withdrawalTransaction.setTransactionType(TransactionType.REFUND);
        withdrawalTransaction.setTransactionDate(LocalDateTime.now());
        withdrawalTransaction.setAmount(refundAmount);
        //account isi ucun rabbitmq gonder
       return transactionRepository.save(withdrawalTransaction);

    }


    public Transaction withdrawBalance(Long id, int amount, Long customerId) {

        Account updatedAccount = accountApiCall(id);
        if (updatedAccount == null) {
            throw new ResourceNotFoundException("Account with given id not found try again with correct details !!");
        }
        BigDecimal currentBalance = updatedAccount.getBalance();
        BigDecimal withdrawAmount = BigDecimal.valueOf(amount);

        if(currentBalance.compareTo(withdrawAmount)<0){
            throw new RuntimeException("Insufficient amount.This operation is not possible");
        }
        BigDecimal newBalance = currentBalance.subtract(withdrawAmount);
        updatedAccount.setBalance(newBalance);
        updatedAccount.setLastActivity(LocalDateTime.now());

        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccountId(updatedAccount.getAccountId());
        withdrawalTransaction.setTransactionType(TransactionType.PURCHASE);
        withdrawalTransaction.setTransactionDate(LocalDateTime.now());
        withdrawalTransaction.setAmount(withdrawAmount);
        //account isi ucun rabbitmq gonder
        return transactionRepository.save(withdrawalTransaction);

    }

//    public void delete(Long id) {
//
//        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found !!"));
//        this.accountRepository.delete(account);
//
//    }

//    public void deleteAccountUsingCustomerId(Long customerId) {
//
//        List<Account> accounts = accountRepository.findByCustomerId(customerId);
//
//        for( Account account : accounts)
//        {
//            this.accountRepository.delete(account);
//        }
//
//
//
//    }
//
//    private static Account toAccount(Account account,AccountDto dto){
//        account = com.transaction_service.entity.Account.builder()
//                .customerId(dto.getCustomerId())
//                .balance(new BigDecimal(100))
//                .accountOpeningDate(LocalDateTime.now())
//                .gsmNumber(dto.getGsmNumber())
//                .accountType(AccountType.SAVINGS)
//                .accountStatus(AccountStatus.ACTIVE)
//                .build();
//        return account;
//    }

    private Account accountApiCall(Long id) {
      return restTemplate.getForObject(accountServiceUrl + id, Account.class);
    }
}
