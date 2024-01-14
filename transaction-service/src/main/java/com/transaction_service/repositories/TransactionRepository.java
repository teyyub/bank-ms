package com.transaction_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transaction_service.entity.Transaction;
import com.transaction_service.entity.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.accountId = :accountId and t.transactionType='PURCHASE'")
    int sumAmountByPurchasedAccountId(@Param("accountId") Long accountId);
    Optional<Transaction> findByIdAndTransactionType(Long id, TransactionType transactionType);
}
