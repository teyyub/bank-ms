package com.transaction_service.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.transaction_service.config.TransactionProducer;
import com.transaction_service.dtos.AccountDto;
import com.transaction_service.dtos.AccountResponseDto;
import com.transaction_service.entity.*;
import com.transaction_service.exceptions.ResourceNotFoundException;
import com.transaction_service.repositories.AccountRepository;
import com.transaction_service.repositories.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService   {

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Value("${transaction.amqp.queue}")
    private String transactionQueue;
    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(AccountService.class);


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionProducer transactionProducer;


    public Account create(AccountDto accountDto) {
        Account account =toAccount(new Account(),accountDto);
        log.info("account entity " +account);
        return accountRepository.save(account);
    }


    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }


    public AccountResponseDto getAccount(Long id) {
        // Getting Accounts from ACCOUNT SERVICE
            Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found try again with correct details !!"));

            // Getting customers from USER SERVICE

//            Customer customer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/" + account.getCustomerId(), Customer.class);
//            Customer customer = restTemplate.getForObject(customerServiceUrl + account.getCustomerId(), Customer.class);

//            account.setCustomer(customer);

            return toAccountResponse(account);

    }

    public List<Account> getAccountByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Account updateAccount(Long id, Account account) {

        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found  try again with correct details!!"));
        newAccount.setAccountType(account.getAccountType());
        newAccount.setLastActivity(LocalDateTime.now());
        return accountRepository.save(newAccount);
    }

    public Account updateAccountBalance(Long id, BigDecimal balance) {

        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found  try again with correct details!!"));
        newAccount.setBalance(balance);
        newAccount.setLastActivity(LocalDateTime.now());
        return accountRepository.save(newAccount);
    }

//    @Transactional
//    public Account addBalance(Long id, int amount, Long customerId) {
//
//
//            Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found try again with correct details !!"));
//
////            Customer customer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/" + customerId, Customer.class);
//         Customer customer = restTemplate.getForObject(customerServiceUrl + customerId, Customer.class);
//
//        if (customer == null) {
//                throw new ResourceNotFoundException("Customer with given id not found try again with correct details !!");
//            }
//
//                BigDecimal newBalance = newAccount.getBalance().add(BigDecimal.valueOf(amount));
//                newAccount.setBalance(newBalance);
//                newAccount.setLastActivity(LocalDateTime.now());
//
//                Transaction transaction = new Transaction();
//                transaction.setAccountId(newAccount.getAccountId());
////                transaction.setLastActivity(new Date());
//                transaction.setAmount(amount);
//                transactionRepository.save(transaction);
//                transactionProducer.sendTo(transactionQueue,transaction);
//                return accountRepository.save(newAccount);
//
//    }

    public Account withdrawBalance(Long id, int amount, Long customerId) {

            Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found try again with correct details !!"));

//            Customer customer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/" + customerId, Customer.class);

        Customer customer = restTemplate.getForObject(customerServiceUrl + customerId, Customer.class);

        if (customer == null) {
                throw new ResourceNotFoundException("Customer with given id not found try again with correct details !!");
            } else {
                if(newAccount.getBalance()
                        .compareTo(BigDecimal.valueOf(amount))<0){
                    throw new RuntimeException("This operation is not possible");
                }
                  int newBalance = newAccount.getBalance().intValue() - amount;
                newAccount.setBalance(new BigDecimal(newBalance));
                newAccount.setLastActivity(LocalDateTime.now());
                return accountRepository.save(newAccount);
            }



    }


    public void delete(Long id) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found !!"));
        this.accountRepository.delete(account);

    }

    public void deleteAccountUsingCustomerId(Long customerId) {

        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        for( Account account : accounts)
        {
            this.accountRepository.delete(account);
        }
    }

    public static AccountResponseDto toAccountResponse(Account ent){
        return AccountResponseDto.builder()
                .accountId(ent.getAccountId())
                .balance(ent.getBalance())
                .build();
    }

    public static Account toAccount(AccountResponseDto dto){
        return Account.builder()
                .balance(dto.getBalance())
                .build();
    }
    public static Account toAccount(Account account,AccountDto dto){
        account = Account.builder()
                .customerId(dto.getCustomerId())
                .balance(new BigDecimal(100))
                .accountOpeningDate(LocalDateTime.now())
                .gsmNumber(dto.getGsmNumber())
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return account;
    }
}
