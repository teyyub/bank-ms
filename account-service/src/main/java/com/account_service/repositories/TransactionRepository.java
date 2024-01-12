package com.account_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account_service.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String> {


}
