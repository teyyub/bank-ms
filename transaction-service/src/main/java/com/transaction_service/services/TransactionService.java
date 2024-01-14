package com.transaction_service.services;

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
import com.transaction_service.entity.Transaction;
import com.transaction_service.entity.TransactionType;
import com.transaction_service.exceptions.ResourceNotFoundException;
import com.transaction_service.payload.ApiResponse;
import com.transaction_service.repositories.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService   {

//    @Value("${customer.service.url}")
//    private String customerServiceUrl;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    @Value("${transaction.amqp.queue}")
    private String transactionQueue;

    @Value("${account.amqp.queue}")
    private String accountTopUp;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AccountProducer accountProducer;
    private Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionProducer transactionProducer;

    @Transactional
    public ApiResponse addBalance(Long id, int amount) {

         AccountResponseDto newAccount = accountApiCall(id);
        System.out.println(newAccount);
        if (newAccount == null) {
            throw new ResourceNotFoundException("Account with given id not found try again with correct details !!");
        }

        BigDecimal amountToAdd = BigDecimal.valueOf(amount);
        if (amountToAdd.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero for a top-up.");
        }
                BigDecimal newBalance = newAccount.getBalance().add(amountToAdd);
                newAccount.setBalance(newBalance);
//                newAccount.setLastActivity(LocalDateTime.now());

                Transaction transaction = new Transaction();
                transaction.setAccountId(newAccount.getAccountId());
                transaction.setTransactionType(TransactionType.TOP_UP);
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setAmount(amountToAdd);
                transactionRepository.save(transaction);

                accountProducer.sendTo(accountTopUp,newAccount);
//                transactionProducer.sendTo(transactionQueue,transaction);
                return null;

    }


    public Transaction refundBalance(Long id,Long transactionId,int amount) {
        AccountResponseDto updatedAccount = accountApiCall(id);
        if (updatedAccount == null) {
                throw new ResourceNotFoundException("Account with given id not found try again with correct details !!");
            }
        Transaction withdrawalTransaction = transactionRepository.findByIdAndTransactionType(transactionId,TransactionType.PURCHASE).orElseThrow(()->new ResourceNotFoundException("Transaction id not found"));
//
            BigDecimal currentBalance = updatedAccount.getBalance();
            BigDecimal refundAmount = BigDecimal.valueOf(amount);
            BigDecimal refundableAmount =withdrawalTransaction.getAmount();
//                    BigDecimal.valueOf(transactionRepository.sumAmountByPurchasedAccountId(id));
            if(refundableAmount.compareTo(refundAmount)<0){
                throw new IllegalArgumentException("This operation is not possible");
            }
            BigDecimal newBalance = currentBalance.add(refundAmount);
            updatedAccount.setBalance(newBalance);

//         withdrawalTransaction.setAccountId(updatedAccount.getAccountId());
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.REFUND);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccountId(id);
        transaction.setAmount(refundAmount);
        accountProducer.sendTo(accountTopUp,updatedAccount);
        transactionRepository.save(transaction);
       return transaction;

    }


    public Transaction withdrawBalance(Long id, int amount  ) {

        AccountResponseDto updatedAccount = accountApiCall(id);
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
//        updatedAccount.setLastActivity(LocalDateTime.now());

        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccountId(updatedAccount.getAccountId());
        withdrawalTransaction.setTransactionType(TransactionType.PURCHASE);
        withdrawalTransaction.setTransactionDate(LocalDateTime.now());
        withdrawalTransaction.setAmount(withdrawAmount);
        accountProducer.sendTo(accountTopUp,updatedAccount);
        return transactionRepository.save(withdrawalTransaction);

    }

    private AccountResponseDto accountApiCall(Long id) {
      return restTemplate.getForObject(accountServiceUrl + id, AccountResponseDto.class);
    }
}
