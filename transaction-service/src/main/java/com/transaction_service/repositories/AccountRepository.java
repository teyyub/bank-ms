package com.transaction_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transaction_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {

    //Find accounts using userId
    List<Account> findByCustomerId(Long customerId);

}
