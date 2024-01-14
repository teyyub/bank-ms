package com.transaction_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transaction_service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    private AccountService accountService;



    //Create Account
//    @PostMapping
//    public ResponseEntity<Account> createAccount(@RequestBody AccountDto dto)
//    {
//        log.info("account request "+dto);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(accountService.create(dto));
//    }

    // Get single Account Details
//    @GetMapping("/{accountId}")
//    public ResponseEntity<Account> getAccount(@PathVariable Long accountId)
//    {
//        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(accountId));
//    }

    // Get Accounts using Customer ID

//    @GetMapping("/user/{customerId}")
//    public ResponseEntity<List<Account>> getAccountsUsingCustomerID(@PathVariable Long customerId)
//    {
//        return ResponseEntity.ok(accountService.getAccountByCustomerId(customerId));
//    }

    // Get all Account Details

//    @GetMapping
//    public ResponseEntity<List<Account>> getAccounts()
//    {
//        return ResponseEntity.ok(accountService.getAccounts());
//    }

    // update account

//    @PutMapping("/{accountID}")
//    public ResponseEntity<Account> updateAccount(@RequestBody Account account,
//                                                 @PathVariable Long accountID){
//
//        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(accountID,account));
//    }

    // Add Money
    @PutMapping("/top-up/{accountId}")
//    @CircuitBreaker(name = "account",fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> addMoney(@PathVariable Long accountId,@RequestParam int amount,  @RequestParam Long customerId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.addBalance(accountId,amount, customerId));
    }


    // withdraw Money
    @PutMapping("/purchase/{accountId}")
//    @CircuitBreaker(name = "customer",fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId,
                                            @RequestParam int amount,
                                            @RequestParam Long customerId)
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.withdrawBalance(accountId,amount, customerId));
    }

    @PutMapping("/refund/{accountId}")
//    @CircuitBreaker(name = "customer",fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> refund(@PathVariable Long accountId,
                                      @RequestParam int amount,
                                      @RequestParam Long customerId)
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.refundBalance(accountId,amount, customerId));
    }

    public ResponseEntity<?> fallbackMethod(Long accountID, int amount,  Long customerId, RuntimeException exception)
    {

        return ResponseEntity.ok("Service is unavailable")  ;
    }

    // Delete Account

//    @DeleteMapping("/{accountId}")
//    public ApiResponse deleteAccount(@PathVariable Long accountId)
//    {
//        this.accountService.delete(accountId);
//        return new ApiResponse("Account is Successfully Deleted", true);
//    }

    // Delete Account using customerId

//    @DeleteMapping("user/{customerId}")
//    public ApiResponse deleteAccountByUserId(@PathVariable Long customerId)
//    {
//        this.accountService.deleteAccountUsingCustomerId(customerId);
//        return new ApiResponse(" Accounts with given userId is deleted Successfully", true);
//
//    }


}
