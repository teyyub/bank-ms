package com.account_service.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.account_service.config.TransactionProducer;
import com.account_service.dtos.AccountDto;
import com.account_service.entity.Account;
import com.account_service.entity.Customer;
import com.account_service.entity.Transaction;
import com.account_service.exceptions.ResourceNotFoundException;
import com.account_service.repositories.AccountRepository;
import com.account_service.repositories.TransactionRepository;

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
//        System.out.println(accountDto);
//        Optional<Account> checkAccount = accountRepository.findByCustomerId(accountDto.getCustomerId()).stream().findAny();
//        if(checkAccount.isPresent()) throw new RuntimeException("This is already registrered");
        Account account = new Account();
//        String accountId = UUID.randomUUID().toString();
//        account.setAccountId(accountId);


        Date current_Date = new Date();
        account.setCustomerId(accountDto.getCustomerId());
        account.setAccountOpeningDate(current_Date);
        account.setLastActivity(current_Date);
        account.setBalance(new BigDecimal(100));
        log.info("account entity " +account);
        return accountRepository.save(account);
    }


    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }


    public Account getAccount(Long id) {

        // Getting Accounts from ACCOUNT SERVICE


            Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found try again with correct details !!"));

            // Getting customers from USER SERVICE

//            Customer customer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/" + account.getCustomerId(), Customer.class);
            Customer customer = restTemplate.getForObject(customerServiceUrl + account.getCustomerId(), Customer.class);

//            account.setCustomer(customer);

            return account;

    }

    public List<Account> getAccountByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Account updateAccount(Long id, Account account) {

        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found  try again with correct details!!"));
        newAccount.setAccountType(account.getAccountType());
        newAccount.setLastActivity(new Date());
        return accountRepository.save(newAccount);
    }

    @Transactional
    public Account addBalance(Long id, int amount, Long customerId) {


            Account newAccount = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with given id not found try again with correct details !!"));

//            Customer customer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/" + customerId, Customer.class);
         Customer customer = restTemplate.getForObject(customerServiceUrl + customerId, Customer.class);

        if (customer == null) {
                throw new ResourceNotFoundException("Customer with given id not found try again with correct details !!");
            }

                BigDecimal newBalance = newAccount.getBalance().add(BigDecimal.valueOf(amount));
                newAccount.setBalance(newBalance);
                newAccount.setLastActivity(new Date());

                Transaction transaction = new Transaction();
                transaction.setAccountId(newAccount.getAccountId());
                transaction.setLastActivity(new Date());
                transaction.setAmount(amount);
                transactionRepository.save(transaction);
                transactionProducer.sendTo(transactionQueue,transaction);
                return accountRepository.save(newAccount);

    }

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
                newAccount.setLastActivity(new Date());
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


}
