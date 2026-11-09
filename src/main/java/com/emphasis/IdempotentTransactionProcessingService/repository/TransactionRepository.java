package com.emphasis.IdempotentTransactionProcessingService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emphasis.IdempotentTransactionProcessingService.model.Transaction;
import com.emphasis.IdempotentTransactionProcessingService.model.Transaction.Status;

public interface TransactionRepository  extends JpaRepository<Transaction,Long> {

    Optional<Transaction> findByIdempotencyKey(
            String idempotencyKey);

    List<Transaction> findByStatus(Status status);
}