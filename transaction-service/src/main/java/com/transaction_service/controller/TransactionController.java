package com.transaction_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transaction_service.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService accountService;


     // Add Money
    @PutMapping("/top-up/{accountId}")
//    @CircuitBreaker(name = "account",fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> addMoney(@PathVariable Long accountId,
                                      @RequestParam int amount)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.addBalance(accountId,amount));
    }


    // withdraw Money
    @PutMapping("/purchase/{accountId}")
//    @CircuitBreaker(name = "customer",fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId,
                                            @RequestParam int amount )
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.withdrawBalance(accountId,amount));
    }

    @PutMapping("/refund/{accountId}")
//    @CircuitBreaker(name = "customer",fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> refund(
            @PathVariable Long accountId,
            @RequestParam Long transactionId,
            @RequestParam int amount )
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.refundBalance(accountId,transactionId,amount));
    }
    public ResponseEntity<?> fallbackMethod(Long accountID, int amount,  Long customerId, RuntimeException exception)
    {

        return ResponseEntity.ok("Service is unavailable")  ;
    }


}
