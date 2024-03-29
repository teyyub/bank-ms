package com.transaction_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transaction_service.dtos.AccountDto;
import com.transaction_service.entity.Account;
import com.transaction_service.payload.ApiResponse;
import com.transaction_service.services.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;


    //Create Account
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto dto)
    {
        log.info("account request "+dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.create(dto));
    }

    // Get single Account Details
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable Long accountId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.getAccount(accountId));
    }

    // Get Accounts using Customer ID

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<Account>> getAccountsUsingCustomerID(@PathVariable Long customerId)
    {
        return ResponseEntity.ok(accountService.getAccountByCustomerId(customerId));
    }

    // Get all Account Details

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts()
    {
        return ResponseEntity.ok(accountService.getAccounts());
    }

    // update account

    @PutMapping("/{accountID}")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account,
                                                 @PathVariable Long accountID){

        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(accountID,account));
    }

//    // Add Money
//    @PutMapping("/top-up/{accountID}")
//    public ResponseEntity<Account> addMoney(@PathVariable Long accountID,@RequestParam int amount,  @RequestParam Long customerId)
//    {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(accountService.addBalance(accountID,amount, customerId));
//    }


//    // withdraw Money
//    @PutMapping("/purchase/{accountID}")
//    @CircuitBreaker(name = "customer",fallbackMethod = "fallbackMethod")
//    public ResponseEntity<Account> withdraw(@PathVariable Long accountID,
//                                            @RequestParam int amount,
//                                            @RequestParam Long customerId)
//    {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(accountService.withdrawBalance(accountID,amount, customerId));
//    }

    public ResponseEntity<?> fallbackMethod(Long accountID, int amount,  Long customerId, RuntimeException exception)
    {
        return ResponseEntity.ok("Service is unavailable")  ;
    }

    // Delete Account

    @DeleteMapping("/{accountId}")
    public ApiResponse deleteAccount(@PathVariable Long accountId)
    {
        this.accountService.delete(accountId);
        return new ApiResponse("Account is Successfully Deleted", true);
    }

    // Delete Account using customerId

    @DeleteMapping("user/{customerId}")
    public ApiResponse deleteAccountByUserId(@PathVariable Long customerId)
    {
        this.accountService.deleteAccountUsingCustomerId(customerId);
        return new ApiResponse(" Accounts with given userId is deleted Successfully", true);

    }


}
