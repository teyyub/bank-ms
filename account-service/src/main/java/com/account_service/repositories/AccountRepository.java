package com.account_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account,String> {

    //Find accounts using userId
    List<Account> findByCustomerId(String customerId);

}
