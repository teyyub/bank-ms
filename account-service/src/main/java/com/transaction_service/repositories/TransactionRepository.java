package com.transaction_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transaction_service.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String> {


}
